package com.yjdev.goforawalk.data.repository

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.yjdev.goforawalk.data.remote.ApiService
import com.yjdev.goforawalk.data.model.IdTokenRequest
import com.yjdev.goforawalk.data.local.TokenManager
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun loginWithKakao(context: Context): LoginResult {
        return try {
            val token = kakaoLogin(context)
            val idToken = token.idToken ?: return LoginResult.Failure("idToken 없음")
            loginToServer(idToken)
        } catch (e: Exception) {
            LoginResult.Failure("카카오 로그인 실패: ${e.message}")
        }
    }

    private suspend fun kakaoLogin(context: Context): OAuthToken =
        suspendCancellableCoroutine { cont ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    cont.resumeWithException(error)
                } else if (token != null) {
                    cont.resume(token)
                } else {
                    cont.resumeWithException(Exception("로그인 결과 없음"))
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }

    private suspend fun loginToServer(idToken: String): LoginResult {
        return try {
            val response = apiService.loginWithOAuth(request = IdTokenRequest(idToken = idToken))
            if (response.isSuccessful) {
                val token = response.body()?.data?.credentials?.accessToken
                if (token != null) {
                    tokenManager.saveToken(token)
                    LoginResult.Success
                } else {
                    LoginResult.Failure("서버 응답에 토큰 없음")
                }
            } else {
                LoginResult.Failure("서버 오류: ${response.code()}")
            }
        } catch (e: Exception) {
            LoginResult.Failure("서버 로그인 실패: ${e.message}")
        }
    }
}


sealed class LoginResult {
    object Success : LoginResult()
    data class Failure(val message: String) : LoginResult()
}