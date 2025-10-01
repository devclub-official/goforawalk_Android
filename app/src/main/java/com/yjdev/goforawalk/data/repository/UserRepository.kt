package com.yjdev.goforawalk.data.repository

import com.yjdev.goforawalk.data.local.TokenManager
import com.yjdev.goforawalk.data.model.NicknameRequest
import com.yjdev.goforawalk.data.remote.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun deleteAccount(): Result<Unit> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("토큰 없음"))

        return kotlin.runCatching {
            val response = apiService.deleteAccount("Bearer $token")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("회원 탈퇴 실패 : ${response.code()}"))
            }
        }.getOrElse {
            Result.failure(it)
        }
    }

    suspend fun updateNickname(newNickname: String): Result<Unit> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("토큰 없음"))

        return kotlin.runCatching {
            val response = apiService.updateNickname("Bearer $token", NicknameRequest(newNickname))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("닉네임 변경 실패 : ${response.code()}"))
            }
        }.getOrElse {
            Result.failure(it)
        }
    }

}