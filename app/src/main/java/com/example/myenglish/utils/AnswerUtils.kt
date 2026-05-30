package com.example.myenglish.utils

import java.util.StringTokenizer

fun cleanAnswer(text: String): String {
    val builder = StringBuilder()
    var lastWasSpace = true
    var i = 0

    while (i < text.length) {
        val currentChar = text[i]
        val lowerChar = Character.toLowerCase(currentChar)

        if (lowerChar >= 'a' && lowerChar <= 'z') {
            builder.append(lowerChar)
            lastWasSpace = false
        } else if (lowerChar >= '0' && lowerChar <= '9') {
            builder.append(lowerChar)
            lastWasSpace = false
        } else if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
            if (!lastWasSpace && builder.isNotEmpty()) {
                builder.append(' ')
                lastWasSpace = true
            }
        }

        i++
    }

    if (builder.isNotEmpty() && builder[builder.length - 1] == ' ') {
        builder.deleteCharAt(builder.length - 1)
    }

    return builder.toString()
}

fun isCorrectAnswer(studentAnswer: String, correctAnswer: String): Boolean {
    val correct = cleanAnswer(studentAnswer) == cleanAnswer(correctAnswer)
    SpokenFeedbackSounds.maybePlay(studentAnswer, correctAnswer, correct)
    return correct
}

fun countWords(text: String): Int {
    val cleanText = cleanAnswer(text)
    if (cleanText == "") return 0
    val tokenizer = StringTokenizer(cleanText, " ")
    return tokenizer.countTokens()
}

fun revealedHintText(correctText: String, hintCount: Int): String {
    val cleanText = cleanAnswer(correctText)
    if (cleanText == "" || hintCount <= 0) return ""

    val tokenizer = StringTokenizer(cleanText, " ")
    val builder = StringBuilder()
    var wordsAdded = 0

    while (tokenizer.hasMoreTokens() && wordsAdded < hintCount) {
        if (builder.isNotEmpty()) builder.append(" ")
        builder.append(tokenizer.nextToken())
        wordsAdded++
    }

    return builder.toString()
}

fun canUseHint(
    answer: String,
    replayCount: Int,
    submittedAnswers: Boolean,
    hintCount: Int,
    correctText: String
): Boolean {
    return submittedAnswers &&
            cleanAnswer(answer) != "" &&
            replayCount >= 5 &&
            hintCount < countWords(correctText)
}
