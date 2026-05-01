package com.example.myenglish.data

data class HomeworkSentence(
    val label: String,
    val correctText: String,
    val startMs: Int,
    val endMs: Int
)