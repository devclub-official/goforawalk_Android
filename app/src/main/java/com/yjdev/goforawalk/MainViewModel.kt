package com.yjdev.goforawalk

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjdev.goforawalk.manager.TokenManager
import com.yjdev.goforawalk.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tokenManager : TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun kakaoLogin(context: Context) {
        viewModelScope.launch {
            loginRepository.loginWithKakao(context) { token, error ->
                if (error != null) {
                    _loginState.value = LoginUiState.Failure(error.message ?: "Unknown Error")
                } else if (token != null) {
                    tokenManager.saveToken(token.accessToken)
                    _loginState.value = LoginUiState.Success(token.accessToken)
                }
            }
        }
    }


}
