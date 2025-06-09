package com.yjdev.goforawalk

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjdev.goforawalk.data.FootStep
import com.yjdev.goforawalk.data.IdTokenRequest
import com.yjdev.goforawalk.data.Profile
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
    private val tokenManager: TokenManager,
    private val apiService: ApiService
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _itemList = MutableStateFlow<List<FootStep>>(emptyList())
    val itemList: StateFlow<List<FootStep>> = _itemList

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    fun getToken(): String? = tokenManager.getToken()

    fun kakaoLogin(context: Context) {
        viewModelScope.launch {
            loginRepository.loginWithKakao(context) { token, error ->
                if (error != null) {
                    _loginState.value = LoginUiState.Failure(error.message ?: "Unknown Error")
                } else if (token != null) {
                    loginToServer(token.idToken.orEmpty())
                }
            }
        }
    }

    private fun loginToServer(idToken: String) {
        viewModelScope.launch {
            try {
                val response = apiService.loginWithOAuth(
                    request = IdTokenRequest(idToken = idToken)
                )
                if (response.isSuccessful) {
                    val loginData = response.body()?.data
                    loginData?.let { data ->
                        val accessToken = data.credentials.accessToken
                        tokenManager.saveToken(accessToken)
                        _loginState.value = LoginUiState.Success
                        Log.d("Login", "User info: ${data.userInfo}")
                    }
                } else {
                    Log.e("Login", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Login", "Exception: ${e.message}")
            }
        }
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: return@launch
                val response = apiService.getProfileWithAuth("Bearer $token")
                _profile.value = response.data
            } catch (e: Exception) {
                Log.e("MainViewModel", "프로필 조회 실패: ${e.message}")
            }
        }
    }

    fun fetchList() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: return@launch
                val response = apiService.getFootStepsWithAuth("Bearer $token")
                _itemList.value = response.data.footsteps
            } catch (e: Exception) {
                Log.e("MainViewModel", "발자취 조회 실패: ${e.message}")
            }
        }
    }

}
