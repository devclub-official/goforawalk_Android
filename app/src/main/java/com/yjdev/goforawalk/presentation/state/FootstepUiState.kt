package com.yjdev.goforawalk.presentation.state

import com.yjdev.goforawalk.data.model.Footstep

data class FootstepUiState(
    val isLoading: Boolean = false,
    val feedList: List<Footstep> = emptyList()
)
