package com.yjdev.goforawalk

import com.yjdev.goforawalk.data.FootStepResponse
import com.yjdev.goforawalk.data.IdTokenRequest
import com.yjdev.goforawalk.data.LoginResponse
import com.yjdev.goforawalk.data.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/v1/auth/login/oauth2/{provider}")
    suspend fun loginWithOAuth(
        @Path("provider") provider: String = "KAKAO",
        @Body request: IdTokenRequest
    ): Response<LoginResponse>

    @GET("/api/v1/footsteps")
    suspend fun getFootStepsWithAuth(
        @Header("Authorization") auth: String
    ): FootStepResponse

    @GET("/api/v1/profile")
    suspend fun getProfileWithAuth(
        @Header("Authorization") auth: String
    ): ProfileResponse
}
