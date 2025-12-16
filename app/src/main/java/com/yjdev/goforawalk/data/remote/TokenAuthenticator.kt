package com.yjdev.goforawalk.data.remote

import com.yjdev.goforawalk.data.local.TokenManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshApi: RefreshApiService
) : Authenticator {

    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        synchronized(lock) {
            val newAccessToken = tokenManager.getAccessToken()
            val requestAccessToken =
                response.request.header("Authorization")?.removePrefix("Bearer ")

            if (newAccessToken != null && newAccessToken != requestAccessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            }

            // 실제 refresh 수행
            val refreshToken = tokenManager.getRefreshToken() ?: return null

            val refreshResponse = refreshApi
                .refreshTokenSync("Bearer $refreshToken")
                .execute()

            if (!refreshResponse.isSuccessful) {
                tokenManager.clearAll()
                return null
            }

            val credentials = refreshResponse.body()
                ?.data
                ?.credentials
                ?: return null

            tokenManager.saveAccessToken(credentials.accessToken)
            tokenManager.saveRefreshToken(credentials.refreshToken)

            return response.request.newBuilder()
                .header("Authorization", "Bearer ${credentials.accessToken}")
                .build()
        }
    }
}

private fun responseCount(response: Response): Int {
    var result = 1
    var priorResponse = response.priorResponse
    while (priorResponse != null) {
        result++
        priorResponse = priorResponse.priorResponse
    }
    return result
}