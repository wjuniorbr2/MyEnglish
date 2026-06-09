package com.example.myenglish.utils

fun cleanAnswer(text: String): String {
    return normalizeFlexibleAnswer(text)
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun normalizeFlexibleAnswer(text: String): String {
    var result = text.lowercase()
        .replace("\u2019", "'")
        .replace("\u2018", "'")
        .replace("\u006