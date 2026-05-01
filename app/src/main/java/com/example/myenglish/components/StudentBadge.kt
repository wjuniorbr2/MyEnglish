package com.example.myenglish.components

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
            .background(Color(0xFFBFC3C8), RoundedCornerShape(14.dp))
            .border(2.dp, Color(0xFF2F3338), RoundedCornerShape(14.dp))
            .clickable { onChangeName() }
            .padding(horizontal = 22.dp, vertical = 9.dp)
    ) {
        Text(
            text = studentName,
            color = Color(0xFF111111),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Cursive,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(Color(0x99FFFFFF), Offset(1.2f, 1.2f), 1.5f)
            )
        )
    }
}
