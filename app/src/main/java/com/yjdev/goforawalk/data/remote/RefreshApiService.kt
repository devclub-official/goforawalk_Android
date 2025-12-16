package com.yjdev.goforawalk.data.remote

import com.yjdev.goforawalk.data.model.RefreshTokenResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshApiService {
    @POST("/api/v1/auth/token/refresh")
    fun refreshTokenSync(
        @Header("Authorization") refreshToken: String
    ): retrofit2.Call<RefreshTokenResponse>
}