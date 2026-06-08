package com.example.myenglish.utils

fun cleanAnswer(text: String): String {
    return normalizeFlexibleAnswer(text)
        .replace(Regex("[^a-z ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun normalizeFlexibleAnswer(text: String): String {
    return text.lowercase()
        .replace("wanna", "want to")
        .replace("gonna", "going to")
        .replace("thanks", "thank you")
        .replace("thx", "thank you")
}

fun isCorrectAnswer(userAnswer: String, correctAnswer: String): Boolean {
    return cleanAnswer(userAnswer) == cleanAnswer(correctAnswer)
}
