package com.example.myenglish.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController

fun Modifier.hideKeyboardOnBackgroundTap(
    focusManager: FocusManager,
    keyboard: SoftwareKeyboardController?
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        focusManager.clearFocus()
        keyboard?.hide()
    }
}
