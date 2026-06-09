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
        .replace("i'm", "i am")
        .replace("you're", "you are")
        .replace("it's", "it is")
        .replace("that's", "that is")
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
