package com.yjdev.goforawalk.data

data class IdTokenRequest(
    val idToken: String
)

data class LoginResponse(
    val data: LoginData
)

data class LoginData(
    val userId: Long,
    val credentials: Credentials,
    val userInfo: UserInfo
)

data class Credentials(
    val accessToken: String,
    val refreshToken: String
)

data class UserInfo(
    val email: String?,
    val nickname: String
)