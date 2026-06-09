package com.example.myenglish.utils

fun cleanAnswer(text: String): String {
    return normalizeFlexibleAnswer(text)
        .replace(Regex("[^a-z ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun normalizeFlexibleAnswer(text: String): String {
    return text.lowercase()
        .replace("’", "'")
        .replace("i'm", "i am")
        .replace("im