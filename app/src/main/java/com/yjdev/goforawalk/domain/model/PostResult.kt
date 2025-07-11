package com.yjdev.goforawalk.domain.model

sealed class PostResult {
    data class Success(val body: Any?) : PostResult()
    data class Failure(val errorMsg: String) : PostResult()
}