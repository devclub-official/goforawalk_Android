package com.yjdev.goforawalk.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjdev.goforawalk.presentation.state.LoginUiState
import com.yjdev.goforawalk.domain.model.PostResult
import com.yjdev.goforawalk.data.model.Footstep
import com.yjdev.goforawalk.data.model.Profile
import com.yjdev.goforawalk.data.repository.FootstepRepository
import com.yjdev.goforawalk.data.repository.LoginRepository
import com.yjdev.goforawalk.data.repository.LoginResult
import com.yjdev.goforawalk.data.local.TokenManager
import com.yjdev.goforawalk.presentation.state.FootstepUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val footstepRepository: FootstepRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _itemList = MutableStateFlow<List<Footstep>>(emptyList())
    val itemList: StateFlow<List<Footstep>> = _itemList

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    private val _postResult = MutableStateFlow<PostResult?>(null)
    val postResult = _postResult.asStateFlow()

    private val _uiState = mutableStateOf(FootstepUiState())
    val uiState: State<FootstepUiState> = _uiState

    fun getToken(): String? = tokenManager.getToken()

    fun kakaoLogin(context: Context) {
        viewModelScope.launch {
            val result = loginRepository.loginWithKakao(context)
            _loginState.value = when (result) {
                is LoginResult.Success -> LoginUiState.Success
                is LoginResult.Failure -> LoginUiState.Failure(result.message)
            }
        }
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            val result = footstepRepository.fetchUserProfile()
            result.onSuccess { _profile.value = it }
                .onFailure { Log.e("ViewModel", "프로필 조회 실패: ${it.message}") }
        }
    }

    fun fetchList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = footstepRepository.fetchFootsteps()
            result.onSuccess {
                _uiState.value = FootstepUiState(
                    isLoading = false,
                    feedList = it
                )
            }.onFailure {
                Log.e("ViewModel", "목록 조회 실패: ${it.message}")
                _uiState.value = FootstepUiState(isLoading = false)
            }
        }
    }

    fun postFootstep(imageFile: File, contentText: String) {
        viewModelScope.launch {
            val result = footstepRepository.postFootstep(imageFile, contentText)
            _postResult.value = result
        }
    }

    fun deleteFootstep(id: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = footstepRepository.deleteFootstep(id)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onError(it.message ?: "삭제 실패") }
            )
        }
    }

    fun resetPostResult() {
        _postResult.value = null
    }
}
