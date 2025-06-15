package com.yjdev.goforawalk

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.yjdev.goforawalk.data.ErrorResponse
import com.yjdev.goforawalk.data.FootStep
import com.yjdev.goforawalk.data.FootStepResponse
import com.yjdev.goforawalk.data.IdTokenRequest
import com.yjdev.goforawalk.data.PostResult
import com.yjdev.goforawalk.data.Profile
import com.yjdev.goforawalk.manager.TokenManager
import com.yjdev.goforawalk.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
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

    private val _postResult = MutableStateFlow<PostResult?>(null)
    val postResult = _postResult.asStateFlow()

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
                    }
                } else {
                    Log.e("Login", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Login", "Exception: ${e.message}")
            }
        }
    }

    fun postFootStep(imageFile: File, contentText: String) {
        viewModelScope.launch {
            val token = getToken() ?: return@launch
            val imagePart = createImagePart(imageFile)
            val contentPart = createContentPart(contentText)

            val response = safeApiCall {
                apiService.postFootstep("Bearer $token", imagePart, contentPart)
            }

            handleResponse(response)
        }
    }

    private fun createImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("data", file.name, requestFile)
    }

    private fun createContentPart(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private suspend fun safeApiCall(call: suspend () -> Response<FootStepResponse>): Response<FootStepResponse>? {
        return try {
            call()
        } catch (e: Exception) {
            Log.e("postFootStep", "API 호출 실패: ${e.message}")
            null
        }
    }

    private fun handleResponse(response: Response<FootStepResponse>?) {
        if (response == null) {
            _postResult.value = PostResult.Failure("네트워크 오류가 발생했습니다.")
            return
        }

        if (response.isSuccessful) {
            _postResult.value = PostResult.Success(response.body())
            Log.d("PostFootstep", "성공! ${response.body()}")
        } else {
            val errorBodyString = response.errorBody()?.string()
            val errorMessage = parseErrorMessage(errorBodyString)
            _postResult.value = PostResult.Failure(errorMessage)
            Log.e("PostFootstep", "실패: ${response.code()} $errorBodyString")
        }
    }

    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrEmpty()) return "알 수 없는 오류 발생"
        return try {
            val gson = Gson()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            errorResponse.message
        } catch (e: Exception) {
            "알 수 없는 오류 발생"
        }
    }


    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: return@launch
                val response = apiService.getProfileWithAuth("Bearer $token")
                _profile.value = response.data
            } catch (e: Exception) {
                Log.e("fetchUserProfile", "프로필 조회 실패: ${e.message}")
            }
        }
    }

    fun fetchList() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken() ?: return@launch
                val response = apiService.getFootStepsWithAuth("Bearer $token")
                _itemList.value = response.data.footsteps
                Log.d("fetchList", "${response.data}")
            } catch (e: Exception) {
                Log.e("fetchList", "발자취 조회 실패: ${e.message}")
            }
        }
    }

}
