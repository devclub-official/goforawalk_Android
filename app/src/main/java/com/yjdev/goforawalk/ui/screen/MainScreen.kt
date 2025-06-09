package com.yjdev.goforawalk.ui.screen

import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.yjdev.goforawalk.data.Feed
import com.yjdev.goforawalk.data.Screen
import com.yjdev.goforawalk.ui.theme.Goforawalk_AndroidTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Certify, Screen.Profile)

    Goforawalk_AndroidTheme {
        val currentRoute = currentRoute(navController)

        Scaffold(
            bottomBar = {
                if (currentRoute in listOf(Screen.Home.route, Screen.Profile.route)) {
                    NavigationBar {
                        items.forEach { screen ->
                            NavigationBarItem(
                                selected = currentRoute == screen.route,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            popUpTo(Screen.Home.route) { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                icon = { Icon(painterResource(id = screen.icon), contentDescription = screen.label) },
                                label = { Text(screen.label) },
                                colors = NavigationBarItemColors(
                                    selectedIconColor = Color(0xFF109624),
                                    selectedTextColor = Color(0xFF109624),
                                    selectedIndicatorColor = Color.Transparent,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray,
                                    disabledIconColor = Color.LightGray,
                                    disabledTextColor = Color.LightGray
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        //TODO 테스트 값 나중에 실제 list값으로 변경 필요
                        listOf(
                            Feed(
                                "닉네임",
                                "3",
                                "2025년 6월 28일",
                                "30분전",
                                "설명..........",
                                "https://images.unsplash.com/photo-1506744038136-46273834b3fb"
                            ),
                            Feed(
                                "닉네임",
                                "3",
                                "2025년 6월 28일",
                                "30분전",
                                "설명..........",
                                "https://images.unsplash.com/photo-1506744038136-46273834b3fb"
                            ),
                            Feed(
                                "닉네임",
                                "3",
                                "2025년 6월 28일",
                                "30분전",
                                "설명..........",
                                "https://images.unsplash.com/photo-1506744038136-46273834b3fb"
                            )
                        )
                    )
                }
                composable(Screen.Certify.route) { CertifyScreen() }
                composable(Screen.Profile.route) { ProfileScreen(onSettingsClick = { navController.navigate(Screen.Settings.route) }) }
                composable(Screen.Settings.route) { SettingsScreen(onBack = { navController.popBackStack() }) }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission() {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }
}
