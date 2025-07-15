package com.yjdev.goforawalk.presentation.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yjdev.goforawalk.presentation.state.LoginUiState
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel
import com.yjdev.goforawalk.presentation.ui.screen.LoginScreen
import com.yjdev.goforawalk.presentation.ui.screen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()
    val token = viewModel.getToken()
    val startDestination = remember {
        if (token.isNullOrEmpty()) {
            "login"
        } else {
            "main"
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { loginState }
            .collect { state ->
                if (state is LoginUiState.Success) {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(viewModel = viewModel)
        }
        composable("main") {
            MainScreen(viewModel = viewModel, rootNavHostController = navController)
        }
    }
}
