package com.example.myenglish.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.Header
import com.example.myenglish.components.HomeworkIcon
import com.example.myenglish.components.StrokeGlowTitle
import com.example.myenglish.data.HomeworkData

@Composable
fun Lesson(
    name: String,
    listeningDone: Boolean,
    showChoices: Boolean,
    showHomework: () -> Unit,
    openBook: () -> Unit,
    openListening: () -> Unit,
    back: () -> Unit
) {
    val listeningAvailable = HomeworkData.hasListeningHomework(name)
    val bookAvailable = name == "Lesson 1" || name == "Lesson 2" || name == "Lesson 3"

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name)
        Spacer(Modifier.height(14.dp))

        ArtButton(
            text = if (bookAvailable) "Book" else "Book - coming soon",
            onClick = openBook,
            enabled = bookAvailable
        )
        Spacer(Modifier.height(7.dp))

        ArtButton("Practice", { })
        Spacer(Modifier.height(7.dp))

        ArtButton(
            text = "Homework",
            onClick = showHomework,
            heightDp = 78,
            content = {
                Text(
                    text = "Homework",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-8).dp),
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                    )
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeworkIconSpot(0, listeningDone)
                    Spacer(Modifier.width(5.dp))
                    HomeworkIconSpot(1, false)
                    Spacer(Modifier.width(5.dp))
                    HomeworkIconSpot(2, false)
                }
            }
        )

        if (showChoices) {
            Spacer(Modifier.height(20.dp))

            StrokeGlowTitle("Choose homework", fontSize = 22)

            Spacer(Modifier.height(8.dp))

            Box(contentAlignment = Alignment.Center) {
                ArtButton(
                    text = if (listeningAvailable) "Listening homework" else "Listening homework - coming soon",
                    onClick = openListening,
                    enabled = listeningAvailable,
                    fontSize = 17
                )

                if (listeningDone) {
                    Image(
                        painter = painterResource(id = R.drawable.donestamp),
                        contentDescription = "Done",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = -88.dp, y = (-16).dp)
                            .size(80.dp)
                            .rotate(-25f)
                    )
                }
            }

            Spacer(Modifier.height(7.dp))

            ArtButton(
                text = "Written homework",
                onClick = { },
                backgroundResId = R.drawable.graybutton,
                enabled = false,
                fontSize = 17
            )

            Spacer(Modifier.height(7.dp))

            ArtButton(
                text = "Spoken homework",
                onClick = { },
                backgroundResId = R.drawable.graybutton,
                enabled = false,
                fontSize = 17
            )
        }

        Spacer(Modifier.height(16.dp))

        ArtButton(
            text = "Back",
            onClick = back,
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
private fun HomeworkIconSpot(kind: Int, done: Boolean) {
    Box(contentAlignment = Alignment.Center) {
        if (done) {
            Box(
                modifier = Modifier
                    .size(29.dp)
                    .background(Color(0x6637D67A), CircleShape)
            )
        }
        HomeworkIcon(kind, done)
    }
}
