package com.yjdev.goforawalk.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yjdev.goforawalk.data.Screen
import com.yjdev.goforawalk.ui.theme.Goforawalk_AndroidTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Certify, Screen.Profile)

    Goforawalk_AndroidTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val currentRoute = currentRoute(navController)
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
        ) { innerPadding ->
            NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
                composable(Screen.Home.route) {
                    HomeScreen(emptyList())
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