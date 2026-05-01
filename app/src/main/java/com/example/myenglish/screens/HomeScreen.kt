package com.example.myenglish.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.Header
import com.example.myenglish.components.StudentBadge

@Composable
fun Home(
    studentName: String,
    onChangeName: () -> Unit,
    openLesson: (String) -> Unit
) {
    val scroll = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header("Book 1")
            Spacer(Modifier.height(12.dp))

            var lessonNumber = 1
            while (lessonNumber <= 31) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val left = lessonNumber

                    ArtButton(
                        text = "Lesson $left",
                        onClick = { openLesson("Lesson $left") },
                        modifier = Modifier.width(145.dp),
                        heightDp = 52,
                        fontSize = 16
                    )

                    if (left + 1 <= 31) {
                        val right = left + 1

                        ArtButton(
                            text = "Lesson $right",
                            onClick = { openLesson("Lesson $right") },
                            modifier = Modifier.width(145.dp),
                            heightDp = 52,
                            fontSize = 16
                        )
                    } else {
                        Spacer(Modifier.width(145.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))
                lessonNumber += 2
            }

            Spacer(Modifier.height(72.dp))
        }

        StudentBadge(
            studentName = studentName,
            onChangeName = onChangeName,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}
