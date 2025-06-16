package com.yjdev.goforawalk.data.model

data class ProfileResponse(
    val data: Profile
)

data class Profile(
    val userId: Int,
    val userNickname: String,
    val userEmail: String?,
    val userProvider: String,
    val totalFootstepCount: Int,
    val footstepStreakDays: Int
)