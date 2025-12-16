package com.yjdev.goforawalk.data.repository

import com.google.gson.Gson
import com.yjdev.goforawalk.data.model.ErrorResponse
import com.yjdev.goforawalk.data.model.Footstep
import com.yjdev.goforawalk.data.model.Profile
import com.yjdev.goforawalk.data.remote.ApiService
import com.yjdev.goforawalk.domain.model.PostResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class FootstepRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchFootsteps(): Result<List<Footstep>> = runCatching {
        val response = apiService.getFootsteps()
        response.data.footsteps
    }

    suspend fun fetchUserProfile(): Result<Profile> = runCatching {
        val response = apiService.getProfile()
        response.data
    }

    suspend fun deleteFootstep(id: Int): Result<Unit> = runCatching {
        val response = apiService.deleteFootstep(id)
        if (!response.isSuccessful) {
            throw Exception("삭제 실패: ${response.code()}")
        }
    }

    suspend fun postFootstep(imageFile: File, content: String): PostResult {
        return try {
            val imagePart = createImagePart(imageFile)
            val contentPart = content.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.postFootstep(imagePart, contentPart)

            if (response.isSuccessful) {
                PostResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

                PostResult.Failure(
                    errorCode = errorResponse?.code,
                    errorMsg = errorResponse?.message ?: "서버 오류"
                )
            }
        } catch (e: Exception) {
            PostResult.Failure(
                errorCode = null,
                errorMsg = e.message ?: "네트워크 오류"
            )
        }
    }

    private fun createImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("data", file.name, requestFile)
    }

    suspend fun checkFootstepAvailability(): PostResult {
        return try {
            val response = apiService.getFootstepAvailability()

            if (response.isSuccessful) {
                PostResult.Success(response.body())
            } else {
                PostResult.Failure(
                    errorCode = response.code().toString(),
                    errorMsg = "서버 오류: ${response.code()}"
                )
            }
        } catch (e: Exception) {
            PostResult.Failure(
                errorCode = null,
                errorMsg = e.message ?: "네트워크 오류"
            )
        }
    }

}
