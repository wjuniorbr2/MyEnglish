package com.example.myenglish.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudentBadge(
    studentName: String,
    onChangeName: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier
            .shadow(10.dp, RoundedCornerShape(14.dp))
            .background(Color(0xFFE7C98F), RoundedCornerShape(14.dp))
            .border(2.dp, Color(0xFF5B3217), RoundedCornerShape(14.dp))
            .clickable { onChangeName() }
            .padding(horizontal = 22.dp, vertical = 9.dp)
    ) {
        Canvas(Modifier.matchParentSize()) {
            val lineColor = Color(0x33FFFFFF)
            var y = 6f
            while (y < size.height) {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y + 8f),
                    strokeWidth = 2f
                )
                y += 16f
            }

            drawCircle(
                color = Color(0x665B3217),
                radius = 5f,
                center = Offset(11f, size.height / 2f)
            )
        }

        Text(
            text = studentName,
            color = Color(0xFF2A1608),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Cursive,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(Color(0x66FFFFFF), Offset(1.2f, 1.2f), 1.5f)
            )
        )
    }
}
