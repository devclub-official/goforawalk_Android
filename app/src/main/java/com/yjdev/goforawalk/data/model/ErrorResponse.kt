package com.yjdev.goforawalk.data.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("detailMessage") val detailMessage: String?
)
