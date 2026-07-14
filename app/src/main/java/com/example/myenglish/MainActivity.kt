package com.example.myenglish

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
        enableEdgeToEdge()
        prepareSplashWindow(window)
        overridePendingTransition(0, 0)

        setContent {
            MyEnglishTheme {
                AppWithSplash(this)
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
