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
            builder.append(lowerChar