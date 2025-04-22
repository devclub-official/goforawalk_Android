package com.yjdev.goforawalk

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoForWalkApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        val nativeAppKey = BuildConfig.KAKAO_NATIVE_APP_KEY

        KakaoSdk.init(this, nativeAppKey)
    }

}