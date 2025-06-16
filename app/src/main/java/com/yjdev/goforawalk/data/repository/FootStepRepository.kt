package com.yjdev.goforawalk.data.repository

import com.google.gson.Gson
import com.yjdev.goforawalk.data.remote.ApiService
import com.yjdev.goforawalk.data.model.ErrorResponse
import com.yjdev.goforawalk.domain.model.PostResult
import com.yjdev.goforawalk.data.model.Footstep
import com.yjdev.goforawalk.data.model.Profile
import com.yjdev.goforawalk.data.local.TokenManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class FootstepRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    private fun getAuthToken(): String? {
        return tokenManager.getToken()?.let { "Bearer $it" }
    }

    suspend fun fetchFootsteps(): Result<List<Footstep>> = runCatching {
        val token = getAuthToken() ?: throw Exception("No token")
        val response = apiService.getFootstepsWithAuth(token)
        response.data.footsteps
    }

    suspend fun fetchUserProfile(): Result<Profile> = runCatching {
        val token = getAuthToken() ?: throw Exception("No token")
        val response = apiService.getProfileWithAuth(token)
        response.data
    }

    suspend fun deleteFootstep(id: Int): Result<Unit> = runCatching {
        val token = getAuthToken() ?: throw Exception("No token")
        val response = apiService.deleteFootstep(token, id)
        if (!response.isSuccessful) {
            throw Exception("삭제 실패: ${response.code()}")
        }
    }

    suspend fun postFootstep(imageFile: File, content: String): PostResult {
        return try {
            val token = getAuthToken() ?: return PostResult.Failure("토큰 없음")
            val imagePart = createImagePart(imageFile)
            val contentPart = content.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.postFootstep(token, imagePart, contentPart)

            if (response.isSuccessful) {
                PostResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)?.message
                PostResult.Failure(errorMessage ?: "서버 오류")
            }
        } catch (e: Exception) {
            PostResult.Failure(e.message ?: "네트워크 오류")
        }
    }

    private fun createImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("data", file.name, requestFile)
    }
}
