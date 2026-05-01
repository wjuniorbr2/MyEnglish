package com.example.myenglish.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun HomeworkIcon(kind: Int, done: Boolean) {
    val iconColor = if (done) {
        if (kind == 0) Color(0xFF00AEEF) else if (kind == 1) Color(0xFFFFB300) else Color(0xFFE91E63)
    } else {
        Color(0xFF888888)
    }

    Canvas(Modifier.size(23.dp)) {
        val w = size.width
        val h = size.height

        drawCircle(Color.White, w * 0.48f, Offset(w / 2f, h / 2f))
        drawCircle(Color.Black, w * 0.48f, Offset(w / 2f, h / 2f), style = Stroke(width = 1.8f))

        if (kind == 0) {
            drawArc(iconColor, 205f, 130f, false, Offset(w * 0.24f, h * 0.20f), Size(w * 0.52f, h * 0.52f), style = Stroke(width = 2.3f))
            drawRoundRect(iconColor, Offset(w * 0.22f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
            drawRoundRect(iconColor, Offset(w * 0.64f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
        } else if (kind == 1) {
            drawRoundRect(iconColor, Offset(w * 0.25f, h * 0.23f), Size(w * 0.50f, h * 0.56f), CornerRadius(4f, 4f), style = Stroke(width = 2f))
            drawLine(iconColor, Offset(w * 0.35f, h * 0.40f), Offset(w * 0.66f, h * 0.40f), strokeWidth = 1.7f)
            drawLine(iconColor, Offset(w * 0.35f, h * 0.54f), Offset(w * 0.66f, h * 0.54f), strokeWidth = 1.7f)
            drawLine(iconColor, Offset(w * 0.25f, h * 0.30f), Offset(w * 0.25f, h * 0.72f), strokeWidth = 2.4f)
        } else {
            drawRoundRect(iconColor, Offset(w * 0.38f, h * 0.18f), Size(w * 0.24f, h * 0.42f), CornerRadius(8f, 8f), style = Stroke(width = 2.1f))
            drawArc(iconColor, 25f, 130f, false, Offset(w * 0.25f, h * 0.40f), Size(w * 0.50f, h * 0.28f), style = Stroke(width = 2f))
            drawLine(iconColor, Offset(w * 0.50f, h * 0.62f), Offset(w * 0.50f, h * 0.78f), strokeWidth = 2f)
            drawLine(iconColor, Offset(w * 0.34f, h * 0.80f), Offset(w * 0.66f, h * 0.80f), strokeWidth = 2f)
        }
    }
}
