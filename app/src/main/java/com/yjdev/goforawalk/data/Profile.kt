package com.yjdev.goforawalk.data

data class ProfileResponse(
    val data: Profile
)

data class Profile(
    val userId: Int,
    val userNickname: String,
    val userEmail: String?,
    val userProvider: String,
    val totalFootStepCount: Int,
    val footStepStreakDays: Int
)