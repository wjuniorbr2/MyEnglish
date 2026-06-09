package com.example.myenglish.utils

fun cleanAnswer(text: String): String {
    return text
        .lowercase()
        .replace("’", "'")
        .replace("‘", "'")
        .replace("wanna", "want to")
        .replace("thanks", "thank you")
        .replace("don't", "do not")
        .replace("doesn't", "does not")
        .replace