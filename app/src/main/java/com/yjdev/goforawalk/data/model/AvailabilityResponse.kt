package com.yjdev.goforawalk.data.model

data class AvailabilityResponse(
    val data: AvailabilityData
)

data class AvailabilityData(
    val canCreateToday: Boolean,
    val todayDate: String,
    val existingFootstep: ExistingFootstep?
)

data class ExistingFootstep(
    val footstepId: Int,
    val imageUrl: String,
    val content: String,
    val createdAt: String
)
