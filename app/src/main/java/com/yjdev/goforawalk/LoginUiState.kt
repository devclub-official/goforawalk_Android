package com.yjdev.goforawalk

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Cancelled : LoginUiState()
    data object Success : LoginUiState()
    data class Failure(val error: String) : LoginUiState()
}