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

    val title = BookAudioItem("LESSON 1", "", BOOK_AUDIO_RES_ID, 790, 1847)
    val verbsTitle = BookAudioItem("VERBS", "", BOOK_AUDIO_RES_ID, 3354, 4184)
    val vocabularyTitle = BookAudioItem("VOCABULARY", "", BOOK_AUDIO_RES_ID, 14251, 15357)
    val expressionsTitle = BookAudioItem("EXPRESSIONS", "", BOOK_AUDIO_RES_ID, 64193, 65398)
    val grammarTitle = BookAudioItem("GRAMMAR", "", BOOK_AUDIO_RES_ID, 79528, 81068)
    val alphabetTitle = BookAudioItem("THE ALPHABET", "", ALPHABET_AUDIO_RES_ID, 0, 1400)

    val verbs = arrayOf(
        BookAudioItem("to drink", "beber", BOOK_AUDIO_RES_ID, 5713, 6616),
        BookAudioItem("to eat", "comer", BOOK_AUDIO_RES_ID, 8551, 9423),
        BookAudioItem("to like", "gostar de", BOOK_AUDIO_RES_ID, 11304, 12245)
    )

    val vocabulary = arrayOf(
        BookAudioItem("I", "eu", BOOK_AUDIO_RES_ID, 17047, 17694),
        BookAudioItem("you", "você, vocês", BOOK_AUDIO_RES_ID, 19049, 19825),
        BookAudioItem("they", "eles, elas", BOOK_AUDIO_RES_ID, 21252, 21979),
        BookAudioItem("we", "nós", BOOK_AUDIO_RES_ID, 23566, 24152),
        BookAudioItem("butter", "manteiga", BOOK_AUDIO_RES_ID, 26154, 26814),
        BookAudioItem("ham", "presunto", BOOK_AUDIO_RES_ID, 28810, 29646),
        BookAudioItem("cheese", "queijo", BOOK_AUDIO_RES_ID, 31488, 32496),
        BookAudioItem("juice", "suco", BOOK_AUDIO_RES_ID, 34535, 35414),
        BookAudioItem("milk", "leite", BOOK_AUDIO_RES_ID, 37584, 38373),
        BookAudioItem("water", "água", BOOK_AUDIO_RES_ID, 40290, 41039),
        BookAudioItem("yes", "sim", BOOK_AUDIO_RES_ID, 42999, 43827),
        BookAudioItem("no", "não", BOOK_AUDIO_RES_ID, 45595, 46186),
        BookAudioItem("and", "e", BOOK_AUDIO_RES_ID, 48270, 49121),
        BookAudioItem("meat", "carne", BOOK_AUDIO_RES_ID, 51013, 51788),
        BookAudioItem("bread", "pão", BOOK_AUDIO_RES_ID, 53900, 54759),
        BookAudioItem("beer", "cerveja", BOOK_AUDIO_RES_ID, 57003, 57720),
        BookAudioItem("wine", "vinho", BOOK_AUDIO_RES_ID, 59562, 60367),
        BookAudioItem("with", "com", BOOK_AUDIO_RES_ID, 62076, 62961)
    )

    val expressions = arrayOf(
        BookAudioItem("in the morning", "de manhã", BOOK_AUDIO_RES_ID, 66983, 68053),
        BookAudioItem("in the afternoon", "à/de tarde", BOOK_AUDIO_RES_ID, 72687, 73803),
        BookAudioItem("in the evening", "à/de noite (tardezinha)", BOOK_AUDIO_RES_ID, 69717, 70805),
        BookAudioItem("at night", "à/de noite (depois de escurecer)", BOOK_AUDIO_RES_ID, 75867, 76756)
    )

    val grammarSentences = arrayOf(
        BookAudioItem("I eat bread.", "Eu como pão.", BOOK_AUDIO_RES_ID, 81999, 83150),
        BookAudioItem("We don’t eat bread.", "Nós não comemos pão.", BOOK_AUDIO_RES_ID, 85417, 86545),
        BookAudioItem("Do they eat bread?", "Eles comem pão?", BOOK_AUDIO_RES_ID, 89084, 90350),
        BookAudioItem("Don’t you eat bread?", "Você não come pão?", BOOK_AUDIO_RES_ID, 92239, 93559),
        BookAudioItem("You drink beer.", "Você bebe cerveja.", BOOK_AUDIO_RES_ID, 95424, 96759),
        BookAudioItem("They don’t drink juice.", "Eles não bebem suco.", BOOK_AUDIO_RES_ID, 98834, 100438),
        BookAudioItem("Do I drink wine?", "Eu bebo vinho?", BOOK_AUDIO_RES_ID, 102324, 103702),
        BookAudioItem("Don’t we drink juice?", "Nós não bebemos suco?", BOOK_AUDIO_RES_ID, 105744, 107307)
    )

    val alphabet = arrayOf(
        BookAudioItem("A", "ei", ALPHABET_AUDIO_RES_ID, 1400, 2600),
        BookAudioItem("B", "bi", ALPHABET_AUDIO_RES_ID, 2600, 3700),
        BookAudioItem("C", "ci", ALPHABET_AUDIO_RES_ID, 3700, 4800),
        BookAudioItem("D", "di", ALPHABET_AUDIO_RES_ID, 4800, 6000),
        BookAudioItem("E", "i", ALPHABET_AUDIO_RES_ID, 6000, 7200),
        BookAudioItem("F", "éf", ALPHABET_AUDIO_RES_ID, 7200, 8400),
        BookAudioItem("G", "dji", ALPHABET_AUDIO_RES_ID, 8400, 9600),
        BookAudioItem("H", "eitch", ALPHABET_AUDIO_RES_ID, 9600, 10800),
        BookAudioItem("I", "ai", ALPHABET_AUDIO_RES_ID, 10800, 12000),
        BookAudioItem("J", "djei", ALPHABET_AUDIO_RES_ID, 12000, 13300),
        BookAudioItem("K", "kei", ALPHABET_AUDIO_RES_ID, 13300, 14600),
        BookAudioItem("L", "él", ALPHABET_AUDIO_RES_ID, 14600, 15900),
        BookAudioItem("M", "em", ALPHABET_AUDIO_RES_ID, 15900, 17200),
        BookAudioItem("N", "en", ALPHABET_AUDIO_RES_ID, 17200, 18500),
        BookAudioItem("O", "ou", ALPHABET_AUDIO_RES_ID, 18500, 19800),
        BookAudioItem("P", "pi", ALPHABET_AUDIO_RES_ID, 19800, 21100),
        BookAudioItem("Q", "quiu", ALPHABET_AUDIO_RES_ID, 21100, 22400),
        BookAudioItem("R", "ar", ALPHABET_AUDIO_RES_ID, 22400, 23700),
        BookAudioItem("S", "és", ALPHABET_AUDIO_RES_ID, 23700, 25000),
        BookAudioItem("T", "ti", ALPHABET_AUDIO_RES_ID, 25000, 26300),
        BookAudioItem("U", "iu", ALPHABET_AUDIO_RES_ID, 26300, 27600),
        BookAudioItem("V", "vi", ALPHABET_AUDIO_RES_ID, 27600, 28900),
        BookAudioItem("W", "dâbliu", ALPHABET_AUDIO_RES_ID, 28900, 30500),
        BookAudioItem("X", "éx", ALPHABET_AUDIO_RES_ID, 30500, 31800),
        BookAudioItem("Y", "uai", ALPHABET_AUDIO_RES_ID, 31800, 33100),
        BookAudioItem("Z", "zi", ALPHABET_AUDIO_RES_ID, 33100, 34500)
    )
}
