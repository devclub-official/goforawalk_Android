package com.yjdev.goforawalk.data

data class FootstepsResponse(
    val data: FootstepData
)

data class FootstepData(
    val footsteps: List<Footstep>
)

data class Footstep(
    val userId: Int,
    val userNickname: String,
    val footstepId: Int,
    val date: String,
    val imageUrl: String,
    val content: String?,
    val createdAt: String
)
