package com.example.myenglish.utils

import android.app.Activity
import android.view.View
import android.view.Window
import android.view.WindowManager

fun prepareSplashWindow(window: Window) {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

fun restoreAppWindow(activity: Activity) {
    val window = activity.window

    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.statusBarColor = android.graphics.Color.rgb(5, 22, 52)
    window.navigationBarColor = android.graphics.Color.rgb(5, 22, 52)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}