package com.example.myenglish.utils

fun cleanAnswer(text: String): String {
    return normalize(text)
        .replace(Regex("[^a-z ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun normalize(text: String): String {
    return text.lowercase()
        .replace("’", "'")
        .replace(Regex("\\bwanna\\b"), "want to")
        .replace(Regex("\\