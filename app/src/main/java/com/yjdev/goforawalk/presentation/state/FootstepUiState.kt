package com.yjdev.goforawalk.presentation.state

import com.yjdev.goforawalk.data.model.Footstep

sealed class FootstepUiState {
    object Loading : FootstepUiState()
    data class Success(val feedList: List<Footstep>) : FootstepUiState()
    data class Failure(val errorMessage: String) : FootstepUiState()
}
