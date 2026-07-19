package com.example.myenglish.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.R

@Composable
fun ArtButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(0.6f),
    backgroundResId: Int = R.drawable.bluebutton,
    enabled: Boolean = true,
    heightDp: Int = 60,
    fontSize: Int = 19,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    if (text == "Go to lesson") {
        Box(Modifier.fillMaxSize()) {
            ArtButton(
                text = "Lesson",
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(66.dp)
                    .padding(end = 4.dp, bottom = 18.dp),
                backgroundResId = backgroundResId,
                enabled = enabled,
                heightDp = 50,
                fontSize = 12,
                content = content
            )
        }
        return
    }

    val actualBackgroundResId = if (enabled) backgroundResId else R.drawable.graybutton

    Box(
        modifier
            .height(heightDp.dp)
            .shadow(7.dp, RoundedCornerShape(18.dp))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.matchParentSize().clip(RoundedCornerShape(18.dp))) {
            Image(
                painter = painterResource(id = actualBackgroundResId),
                contentDescription = null,
                modifier = Modifier.matchParentSize().graphicsLayer(scaleX = 1.42f, scaleY = 2.80f),
                contentScale = ContentScale.Crop
            )
            Canvas(Modifier.matchParentSize()) {
                drawRoundRect(
                    Color(0x28FFFFFF),
                    Offset(6f, 4f),
                    Size(size.width - 12f, size.height * 0.06f),
                    CornerRadius(18f, 18f)
                )
                drawRoundRect(
                    Color(0x30000000),
                    Offset(6f, size.height * 0.91f),
                    Size(size.width - 12f, size.height * 0.04f),
                    CornerRadius(18f, 18f)
                )
                drawRoundRect(
                    Color.White,
                    Offset(2f, 2f),
                    Size(size.width - 4f, size.height - 4f),
                    CornerRadius(18f, 18f),
                    style = Stroke(width = 1.4f)
                )
            }
        }

        if (content == null) {
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                )
            )
        } else {
            content()
        }
    }
}
