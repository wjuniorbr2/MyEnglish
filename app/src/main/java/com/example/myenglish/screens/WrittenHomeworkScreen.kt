package com.example.myenglish.screens

import androidx.compose.runtime.Composable
import com.example.myenglish.data.WrittenHomeworkSentence

@Composable
fun WrittenHomework(
    name: String,
    studentName: String,
    sentences: Array<WrittenHomeworkSentence>,
    done: () -> Unit,
    back: () -> Unit
) {
    back()
}
