package com.example.myenglish.screens

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myenglish.AppRoot
import com.example.myenglish.R
import com.example.myenglish.components.AppBackground
import com.example.myenglish.utils.restoreAppWindow

@Composable
fun AppWithSplash(activity: Activity) {
    var splashVisible by remember { mutableStateOf(true) }
    var alphaTarget by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(alphaTarget, animationSpec = tween(950), label = "splashAlpha")

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alphaTarget = 1f }, 420)
        handler.postDelayed({ alphaTarget = 0f }, 2750)
        handler.postDelayed({ splashVisible = false; restoreAppWindow(activity) }, 3950)
        onDispose { handler.removeCallbacksAndMessages(null) }
    }

    if (splashVisible) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.appsplash),
                contentDescription = "My English splash",
                modifier = Modifier.fillMaxWidth(0.92f).alpha(alpha),
                contentScale = ContentScale.Fit
            )
        }
    } else {
        AppBackground {
            AppRoot()
        }
    }
}
