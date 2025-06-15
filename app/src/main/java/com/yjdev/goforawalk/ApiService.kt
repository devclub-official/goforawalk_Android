package com.yjdev.goforawalk

import com.yjdev.goforawalk.data.FootStepResponse
import com.yjdev.goforawalk.data.FootStepsResponse
import com.yjdev.goforawalk.data.IdTokenRequest
import com.yjdev.goforawalk.data.LoginResponse
import com.yjdev.goforawalk.data.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("/api/v1/auth/login/oauth2/{provider}")
    suspend fun loginWithOAuth(
        @Path("provider") provider: String = "KAKAO",
        @Body request: IdTokenRequest
    ): Response<LoginResponse>

    @Multipart
    @POST("/api/v1/footsteps")
    suspend fun postFootstep(
        @Header("Authorization") auth: String,
        @Part image: MultipartBody.Part,
        @Part("content") content: RequestBody
    ): Response<FootStepResponse>

    @GET("/api/v1/footsteps")
    suspend fun getFootStepsWithAuth(
        @Header("Authorization") auth: String
    ): FootStepsResponse

    @GET("/api/v1/profile")
    suspend fun getProfileWithAuth(
        @Header("Authorization") auth: String
    ): ProfileResponse
}
