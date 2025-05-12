package com.yjdev.goforawalk.data

data class Feed(
    val nickname: String,
    val streak: String,
    val date: String,
    val timeAgo: String,
    val description: String,
    val imageUrl: String = ""
)
