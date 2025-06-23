package com.yjdev.goforawalk.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel
import com.yjdev.goforawalk.R
import com.yjdev.goforawalk.presentation.ui.component.LoginButton
import com.yjdev.goforawalk.presentation.ui.theme.Black3A1D1D
import com.yjdev.goforawalk.presentation.ui.theme.Gray757990
import com.yjdev.goforawalk.presentation.ui.theme.YellowFCF4E7
import com.yjdev.goforawalk.presentation.ui.theme.YellowFEE500

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .background(YellowFCF4E7)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(250.dp))
        Image(
            painter = painterResource(R.drawable.ic_app_logo),
            contentDescription = "app_logo",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.login_sub_title),
            color = Gray757990,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        LoginButton(YellowFEE500, Black3A1D1D, YellowFEE500, stringResource(R.string.login_kakao), viewModel = viewModel)
        Spacer(modifier = Modifier.height(150.dp))
    }
}