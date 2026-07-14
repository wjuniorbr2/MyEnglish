package com.example.myenglish

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myenglish.screens.AppWithSplash
import com.example.myenglish.ui.theme.MyEnglishTheme
import com.example.myenglish.utils.prepareSplashWindow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyNoTransition()
        enableEdgeToEdge()
        prepareSplashWindow(window)

        setContent {
            MyEnglishTheme {
                AppWithSplash(this)
            }
        }
    }

    override fun finish() {
        super.finish()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            applyLegacyNoTransition()
        }
    }

    private fun applyNoTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                0,
                0
            )

            overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_CLOSE,
                0,
                0
            )
        } else {
            applyLegacyNoTransition()
        }
    }

    @Suppress("DEPRECATION")
    private fun applyLegacyNoTransition() {
        overridePendingTransition(0, 0)
    }
}