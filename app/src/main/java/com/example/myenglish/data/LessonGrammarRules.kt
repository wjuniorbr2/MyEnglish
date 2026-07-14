package com.example.myenglish.data

private val earlyVerbToBePattern = Regex(
    pattern = """(?i)(?:\b(?:am|is|are|was|were|be|been|being)\b|\b(?:i['’]m|you['’]re|he['’]s|she['’]s|it['’]s|we['’]re|they['’]re|that['’]s|there['’]s)\b)"""
)

internal fun containsEarlyVerbToBe(text: String): Boolean {
    return earlyVerbToBePattern.containsMatchIn(text)
}
