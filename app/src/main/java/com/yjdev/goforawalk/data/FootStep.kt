package com.yjdev.goforawalk.data

data class FootStepsResponse(
    val data: FootStepData
)

data class FootStepData(
    val footsteps: List<FootStep>
)

data class FootStep(
    val userId: Int,
    val userNickname: String,
    val footStepId: Int,
    val date: String,
    val imageUrl: String,
    val content: String?,
    val createdAt: String
)
