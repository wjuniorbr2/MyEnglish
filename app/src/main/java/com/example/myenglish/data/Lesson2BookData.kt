package com.example.myenglish.data

import com.example.myenglish.R

object Lesson2BookData : BookLessonData {
    val BOOK_AUDIO_RES_ID = R.raw.booklesson2
    val ALPHABET_AUDIO_RES_ID = R.raw.alphabet

    override val bookAudioResId = BOOK_AUDIO_RES_ID
    override val alphabetAudioResId = ALPHABET_AUDIO_RES_ID

    override val title = BookAudioItem("LESSON 2", "", BOOK_AUDIO_RES_ID, 0, 2595, true)
    override val verbsTitle = BookAudioItem("VERBS", "", BOOK_AUDIO_RES_ID, 2595, 5200, true)
    override val vocabularyTitle = BookAudioItem("VOCABULARY", "", BOOK_AUDIO_RES_ID, 13095, 16160, true)
    override val expressionsTitle = BookAudioItem("EXPRESSIONS", "", BOOK_AUDIO_RES_ID, 66120, 68675, true)
    override val grammarTitle = BookAudioItem("GRAMMAR", "", BOOK_AUDIO_RES_ID, 95040, 97625, true)
    override val alphabetTitle = Lesson1BookData.alphabetTitle

    override val grammarNoteText = ""
    override val grammarInfoText = "• Os adjetivos em inglês aparecem antes dos substantivos (ex.: good music).\n\n• Idiomas e nacionalidades sempre são escritos com letra maiúscula. E também a palavra I (eu)."

    override val verbs = arrayOf(
        BookAudioItem("to study", "estudar", BOOK_AUDIO_RES_ID, 5200, 7730, true),
        BookAudioItem("to speak", "falar", BOOK_AUDIO_RES_ID, 7730, 10395, true),
        BookAudioItem("to want", "querer", BOOK_AUDIO_RES_ID, 10395, 13095, true)
    )

    override val vocabulary = arrayOf(
        BookAudioItem("here", "aqui, cá", BOOK_AUDIO_RES_ID, 16160, 18625, true),
        BookAudioItem("French", "francês", BOOK_AUDIO_RES_ID, 35175, 37935, true),
        BookAudioItem("hot", "quente", BOOK_AUDIO_RES_ID, 53530, 56040, true),
        BookAudioItem("there", "lá, ali, aí", BOOK_AUDIO_RES_ID, 18625, 21155, true),
        BookAudioItem("German", "alemão", BOOK_AUDIO_RES_ID, 37935, 40900, true),
        BookAudioItem("cold", "frio, gelado", BOOK_AUDIO_RES_ID, 56040, 58395, true),
        BookAudioItem("my", "meu, minha, meus, minhas", BOOK_AUDIO_RES_ID, 21155, 23775, true),
        BookAudioItem("math", "matemática", BOOK_AUDIO_RES_ID, 40900, 43730, true),
        BookAudioItem("big", "grande", BOOK_AUDIO_RES_ID, 58395, 60875, true),
        BookAudioItem("your", "seu, sua, seus, suas", BOOK_AUDIO_RES_ID, 23775, 26575, true),
        BookAudioItem("music", "música", BOOK_AUDIO_RES_ID, 43730, 46360, true),
        BookAudioItem("small", "pequeno", BOOK_AUDIO_RES_ID, 60875, 63320, true),
        BookAudioItem("English", "inglês", BOOK_AUDIO_RES_ID, 26575, 29350, true),
        BookAudioItem("good", "bom", BOOK_AUDIO_RES_ID, 46360, 48760, true),
        BookAudioItem("with me", "comigo", BOOK_AUDIO_RES_ID, 63320, 66120, true),
        BookAudioItem("Spanish", "espanhol", BOOK_AUDIO_RES_ID, 29350, 32195, true),
        BookAudioItem("bad", "ruim, mal, mau", BOOK_AUDIO_RES_ID, 48760, 51185, true),
        BookAudioItem("a", "um, uma (artigo numeral)", BOOK_AUDIO_RES_ID, 68675, 71495, true),
        BookAudioItem("Portuguese", "português", BOOK_AUDIO_RES_ID, 32195, 35175, true),
        BookAudioItem("or", "ou", BOOK_AUDIO_RES_ID, 51185, 53530, true)
    )

    override val expressions = arrayOf(
        BookAudioItem("hi", "oi", BOOK_AUDIO_RES_ID, 71495, 73770, true),
        BookAudioItem("hello", "olá", BOOK_AUDIO_RES_ID, 73770, 76370, true),
        BookAudioItem("what’s up?", "e aí?, beleza?", BOOK_AUDIO_RES_ID, 76370, 78970, true),
        BookAudioItem("good morning", "bom dia", BOOK_AUDIO_RES_ID, 83945, 86770, true),
        BookAudioItem("bye", "tchau", BOOK_AUDIO_RES_ID, 78970, 81435, true),
        BookAudioItem("good afternoon", "boa tarde", BOOK_AUDIO_RES_ID, 86770, 89640, true),
        BookAudioItem("see you (see ya)", "até mais (falou)", BOOK_AUDIO_RES_ID, 81435, 83945, true),
        BookAudioItem("good evening", "boa noite (oi)", BOOK_AUDIO_RES_ID, 89640, 92295, true),
        BookAudioItem("good night", "boa noite (tchau)", BOOK_AUDIO_RES_ID, 92295, 95040, true)
    )

    override val grammarSentences = arrayOf(
        BookAudioItem("I study good music.", "Eu estudo música boa.", BOOK_AUDIO_RES_ID, 97625, 101145, true),
        BookAudioItem("I want good music.", "Eu quero música boa.", BOOK_AUDIO_RES_ID, 111850, 114890, true),
        BookAudioItem("Do you like hot milk?", "Você gosta de leite quente?", BOOK_AUDIO_RES_ID, 101145, 104580, true),
        BookAudioItem("They drink cold juice.", "Eles bebem suco gelado.", BOOK_AUDIO_RES_ID, 114890, 118130, true),
        BookAudioItem("I don’t speak good English.", "Eu não falo um bom inglês.", BOOK_AUDIO_RES_ID, 104580, 108485, true),
        BookAudioItem("You don’t eat a small cheese.", "Você não come um queijo pequeno.", BOOK_AUDIO_RES_ID, 118130, 121410, true),
        BookAudioItem("Do you want a big bread?", "Você quer um pão grande?", BOOK_AUDIO_RES_ID, 108485, 111850, true),
        BookAudioItem("Don’t you want a good English?", "Você não quer um bom inglês?", BOOK_AUDIO_RES_ID, 121410, 124720, true)
    )

    override val alphabet = Lesson1BookData.alphabet
}
