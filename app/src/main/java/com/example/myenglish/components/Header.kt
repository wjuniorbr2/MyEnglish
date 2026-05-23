package com.example.myenglish.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.BuildConfig

@Composable
fun Header(leftTitle: String, subtitle: String? = null) {
    val context = LocalContext.current
    val titleId = remember { context.resources.getIdentifier("title", "drawable", context.packageName) }
    var showAppInfoDialog by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth().height(128.dp)) {
        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(start = 14.dp, top = if (subtitle == null) 30.dp else 22.dp)
        ) {
            StrokeGlowTitle(leftTitle)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                StrokeGlowTitle(subtitle, fontSize = 24)
            }
        }

        if (titleId != 0) {
            Image(
                painter = painterResource(id = titleId),
                contentDescription = "My English",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 46.dp)
                    .width(306.dp)
                    .height(127.dp)
                    .clickable { showAppInfoDialog = true },
                contentScale = ContentScale.Fit
            )
        } else {
            StrokeGlowTitle(
                "My English",
                Modifier.align(Alignment.TopEnd).padding(top = 30.dp),
                26
            )
        }

        if (showAppInfoDialog) {
            AlertDialog(
                onDismissRequest = { showAppInfoDialog = false },
                text = {
                    Text(
                        text = "My English\nAndroid app\nVersion ${BuildConfig.VERSION_NAME}\nBy Waldomiro S C Júnior"
                    )
                },
                confirmButton = {
                    Button(onClick = { showAppInfoDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun StrokeGlowTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 28
) {
    Box(modifier.padding(top = 6.dp)) {
        val style = LocalTextStyle.current.copy(
            fontFamily = FontFamily.Serif,
            shadow = Shadow(Color.White, Offset(0f, 0f), 13f)
        )

        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(3.dp, 3.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset((-3).dp, 0.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(3.dp, 0.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(0.dp, (-3).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(0.dp, 3.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset((-2).dp, (-2).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(2.dp, (-2).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset((-2).dp, 2.dp))
        Text(text, color = Color.White, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style)
    }
}
