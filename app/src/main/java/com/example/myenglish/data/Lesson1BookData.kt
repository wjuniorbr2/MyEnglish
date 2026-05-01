package com.example.myenglish.data

import com.example.myenglish.R

data class BookAudioItem(
    val english: String,
    val translation: String,
    val audioResId: Int,
    val startMs: Int,
    val endMs: Int
)

object Lesson1BookData {
    val BOOK_AUDIO_RES_ID = R.raw.booklesson1
    val ALPHABET_AUDIO_RES_ID = R.raw.alphabet

    val title = BookAudioItem("LESSON 1", "", BOOK_AUDIO_RES_ID, 805, 1785)
    val verbsTitle = BookAudioItem("VERBS", "", BOOK_AUDIO_RES_ID, 3370, 4125)
    val vocabularyTitle = BookAudioItem("VOCABULARY", "", BOOK_AUDIO_RES_ID, 13365, 15315)
    val expressionsTitle = BookAudioItem("EXPRESSIONS", "", BOOK_AUDIO_RES_ID, 62095, 62950)
    val grammarTitle = BookAudioItem("GRAMMAR", "", BOOK_AUDIO_RES_ID, 72700, 73750)

    val verbs = arrayOf(
        BookAudioItem("to drink", "beber", BOOK_AUDIO_RES_ID, 5725, 6555),
        BookAudioItem("to eat", "comer", BOOK_AUDIO_RES_ID, 8560, 9360),
        BookAudioItem("to like", "gostar de", BOOK_AUDIO_RES_ID, 11315, 12190)
    )

    val vocabulary = arrayOf(
        BookAudioItem("I", "eu", BOOK_AUDIO_RES_ID, 17060, 17645),
        BookAudioItem("you", "você, vocês", BOOK_AUDIO_RES_ID, 19065, 19840),
        BookAudioItem("they", "eles, elas", BOOK_AUDIO_RES_ID, 21265, 21945),
        BookAudioItem("we", "nós", BOOK_AUDIO_RES_ID, 23580, 24090),
        BookAudioItem("butter", "manteiga", BOOK_AUDIO_RES_ID, 26155, 26770),
        BookAudioItem("ham", "presunto", BOOK_AUDIO_RES_ID, 28805, 29625),
        BookAudioItem("cheese", "queijo", BOOK_AUDIO_RES_ID, 31500, 32430),
        BookAudioItem("juice", "suco", BOOK_AUDIO_RES_ID, 34545, 35345),
        BookAudioItem("milk", "leite", BOOK_AUDIO_RES_ID, 37600, 38315),
        BookAudioItem("water", "água", BOOK_AUDIO_RES_ID, 40305, 40990),
        BookAudioItem("yes", "sim", BOOK_AUDIO_RES_ID, 43010, 43765),
        BookAudioItem("no", "não", BOOK_AUDIO_RES_ID, 45610, 46120),
        BookAudioItem("and", "e", BOOK_AUDIO_RES_ID, 48285, 49070),
        BookAudioItem("meat", "carne", BOOK_AUDIO_RES_ID, 51030, 51725),
        BookAudioItem("bread", "pão", BOOK_AUDIO_RES_ID, 53915, 54700),
        BookAudioItem("beer", "cerveja", BOOK_AUDIO_RES_ID, 57020, 57670),
        BookAudioItem("wine", "vinho", BOOK_AUDIO_RES_ID, 59580, 60330),
        BookAudioItem("with", "com", BOOK_AUDIO_RES_ID, 64105, 65345)
    )

    val expressions = arrayOf(
        BookAudioItem("in the morning", "de manhã", BOOK_AUDIO_RES_ID, 67000, 68060),
        BookAudioItem("in the afternoon", "à/de tarde", BOOK_AUDIO_RES_ID, 69735, 70735),
        BookAudioItem("in the evening", "à/de noite (tardezinha)", BOOK_AUDIO_RES_ID, 75785, 76695),
        BookAudioItem("at night", "à/de noite (depois de escurecer)", BOOK_AUDIO_RES_ID, 78890, 79620)
    )

    val grammarSentences = arrayOf(
        BookAudioItem("I eat bread.", "Eu como pão.", BOOK_AUDIO_RES_ID, 82015, 83180),
        BookAudioItem("You drink beer.", "Você bebe cerveja.", BOOK_AUDIO_RES_ID, 85430, 86490),
        BookAudioItem("We don’t eat bread.", "Nós não comemos pão.", BOOK_AUDIO_RES_ID, 89100, 90305),
        BookAudioItem("They don’t drink juice.", "Eles não bebem suco.", BOOK_AUDIO_RES_ID, 92225, 93500),
        BookAudioItem("Do they eat bread?", "Eles comem pão?", BOOK_AUDIO_RES_ID, 95440, 96715),
        BookAudioItem("Do I drink wine?", "Eu bebo vinho?", BOOK_AUDIO_RES_ID, 98850, 100440),
        BookAudioItem("Don’t you eat bread?", "Você não come pão?", BOOK_AUDIO_RES_ID, 102335, 103635),
        BookAudioItem("Don’t we drink juice?", "Nós não bebemos suco?", BOOK_AUDIO_RES_ID, 105740, 107255)
    )

    val alphabet = arrayOf(
        BookAudioItem("A", "ei", ALPHABET_AUDIO_RES_ID, 725, 1785),
        BookAudioItem("B", "bi", ALPHABET_AUDIO_RES_ID, 4055, 4845),
        BookAudioItem("C", "ci", ALPHABET_AUDIO_RES_ID, 6125, 6685),
        BookAudioItem("D", "di", ALPHABET_AUDIO_RES_ID, 8315, 9000),
        BookAudioItem("E", "i", ALPHABET_AUDIO_RES_ID, 10605, 11265),
        BookAudioItem("F", "éf", ALPHABET_AUDIO_RES_ID, 12925, 13465),
        BookAudioItem("G", "dji", ALPHABET_AUDIO_RES_ID, 15285, 15850),
        BookAudioItem("H", "eitch", ALPHABET_AUDIO_RES_ID, 17545, 18175),
        BookAudioItem("I", "ai", ALPHABET_AUDIO_RES_ID, 19905, 20675),
        BookAudioItem("J", "djei", ALPHABET_AUDIO_RES_ID, 22205, 22835),
        BookAudioItem("K", "kei", ALPHABET_AUDIO_RES_ID, 24620, 25245),
        BookAudioItem("L", "él", ALPHABET_AUDIO_RES_ID, 27035, 27640),
        BookAudioItem("M", "em", ALPHABET_AUDIO_RES_ID, 29325, 30010),
        BookAudioItem("N", "en", ALPHABET_AUDIO_RES_ID, 31505, 32150),
        BookAudioItem("O", "ou", ALPHABET_AUDIO_RES_ID, 33610, 34355),
        BookAudioItem("P", "pi", ALPHABET_AUDIO_RES_ID, 35900, 36395),
        BookAudioItem("Q", "quiu", ALPHABET_AUDIO_RES_ID, 38335, 38755),
        BookAudioItem("R", "ar", ALPHABET_AUDIO_RES_ID, 40600, 41165),
        BookAudioItem("S", "és", ALPHABET_AUDIO_RES_ID, 43015, 43645),
        BookAudioItem("T", "ti", ALPHABET_AUDIO_RES_ID, 45285, 46040),
        BookAudioItem("U", "iu", ALPHABET_AUDIO_RES_ID, 47685, 48250),
        BookAudioItem("V", "vi", ALPHABET_AUDIO_RES_ID, 49895, 50495),
        BookAudioItem("W", "dâbliu", ALPHABET_AUDIO_RES_ID, 52285, 52905),
        BookAudioItem("X", "éx", ALPHABET_AUDIO_RES_ID, 54570, 55245),
        BookAudioItem("Y", "uai", ALPHABET_AUDIO_RES_ID, 56880, 57650),
        BookAudioItem("Z", "zi", ALPHABET_AUDIO_RES_ID, 59210, 59900)
    )
}
