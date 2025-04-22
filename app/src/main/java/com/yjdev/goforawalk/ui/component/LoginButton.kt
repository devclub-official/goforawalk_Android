package com.yjdev.goforawalk.ui.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yjdev.goforawalk.LoginUiState
import com.yjdev.goforawalk.MainViewModel

@Composable
fun LoginButton(containerColor: Color, contentColor: Color, borderColor: Color, text: String) {
    val viewModel: MainViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    Button(
        onClick = {
            viewModel.kakaoLogin(context)
        },
        modifier = Modifier.size(width = 240.dp, height = 54.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        border = BorderStroke(1.dp, color = borderColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text
        )
    }
    when (loginState) {
        is LoginUiState.Idle -> {
        }

        is LoginUiState.Success -> {
            Log.i("LoginButton", "로그인 성공: ${(loginState as LoginUiState.Success).accessToken}")
        }

        is LoginUiState.Failure -> {
            Log.e("LoginButton", "로그인 실패: ${(loginState as LoginUiState.Failure).error}")
        }

        is LoginUiState.Cancelled -> {
            Log.i("LoginButton", "로그인 취소")
        }
    }
}