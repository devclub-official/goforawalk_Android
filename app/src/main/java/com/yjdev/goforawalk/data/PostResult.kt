package com.yjdev.goforawalk.data

sealed class PostResult {
    data class Success(val body: Any?) : PostResult()
    data class Failure(val errorMsg: String) : PostResult()
}