package com.yjdev.goforawalk.data.remote

import android.util.Log
import com.yjdev.goforawalk.data.local.TokenManager
import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        val accessToken = tokenManager.getAccessToken()

        val request = if (!accessToken.isNullOrEmpty()) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
