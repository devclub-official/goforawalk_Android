package com.yjdev.goforawalk

import com.yjdev.goforawalk.data.IdTokenRequest
import com.yjdev.goforawalk.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("/api/v1/auth/login/oauth2/{provider}")
    suspend fun loginWithOAuth(
        @Path("provider") provider: String = "KAKAO",
        @Body request: IdTokenRequest
    ): Response<LoginResponse>
}
