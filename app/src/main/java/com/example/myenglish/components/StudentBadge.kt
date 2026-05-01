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
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color.White, RoundedCornerShape(14.dp))
            .padding(2.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .clickable { onChangeName() }
            .padding(horizontal = 22.dp, vertical = 8.dp)
    ) {
        Text(
            text = studentName,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Cursive,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(Color(0x99000000), Offset(1.7f, 1.7f), 2f)
            )
        )
    }
}
