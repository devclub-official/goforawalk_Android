package com.yjdev.goforawalk.presentation.state

import com.yjdev.goforawalk.R

sealed class Screen(val route: String, val icon: Int, val label: String) {
    object Home : Screen("home", R.drawable.ic_home_24, "홈")
    object Certify : Screen("certify", R.drawable.ic_certify_24, "인증")
    object Profile : Screen("profile", R.drawable.ic_profile_24, "프로필")
    object Settings : Screen("settings", R.drawable.ic_profile_24, "설정")
}