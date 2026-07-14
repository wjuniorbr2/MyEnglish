package com.example.myenglish.utils

import android.app.Activity
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun prepareSplashWindow(window: Window) {
    WindowCompat.getInsetsController(
        window,
        window.decorView
    ).apply {
        hide(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun restoreAppWindow(activity: Activity) {
    val window = activity.window

    WindowCompat.getInsetsController(
        window,
        window.decorView
    ).apply {
        show(WindowInsetsCompat.Type.systemBars())
        isAppearanceLightStatusBars = false
        isAppearanceLightNavigationBars = false
    }
}