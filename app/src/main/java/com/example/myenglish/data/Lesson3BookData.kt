package com.example.myenglish.data

import com.example.myenglish.R

object Lesson3BookData : BookLessonData {
    val BOOK_AUDIO_RES_ID = R.raw.booklesson3
    val ALPHABET_AUDIO_RES_ID = R.raw.alphabet

    override val bookAudioResId = BOOK_AUDIO_RES_ID
    override val alphabetAudioResId = ALPHABET_AUDIO_RES_ID

    override val title = BookAudioItem("LESSON 3", "", BOOK_AUDIO_RES_ID, 0, 2470, true)
    override val verbsTitle = BookAudioItem("VERBS", "", BOOK_AUDIO_RES_ID, 2470, 5235, true)
    override val vocabularyTitle = BookAudioItem("VOCABULARY", "", BOOK_AUDIO_RES_ID, 12865, 15905, true)
    override val expressionsTitle = BookAudioItem("EXPRESSIONS", "", BOOK_AUDIO_RES_ID, 71395, 74120, true)
    override val grammarTitle = BookAudioItem("GRAMMAR", "", BOOK_AUDIO_RES_ID, 88990, 91655, true)
    override val alphabetTitle = Lesson1BookData.alphabetTitle

    override val grammarNoteText = ""
    override val grammarInfoText = "Uso das preposições IN, AT, ON e TO – As preposições IN, AT e ON podem ter traduções parecidas em português, como em, no ou na, mas cada uma é usada de acordo com o sentido da frase.\n\nIN é usado, em geral, com a ideia de dentro de um espaço, em uma área ou em um lugar mais amplo.\nEx.: in the room, in Brazil, in the city.\n\nON é usado, em geral, com a ideia de algo em cima de uma superfície, sobre algo, ou em certas expressões de localização.\nEx.: on the table, on the wall, on the street.\n\nAT é usado, em geral, para indicar um ponto específico ou um local exato.\nEx.: at school, at home, at the door.\n\nTO é usado quando há ideia de movimento ou deslocamento em direção a algum lugar, geralmente com verbos como go, come, travel, walk etc.\nEx.: go to school, travel to Brazil.\n\nNa maioria dos casos, essas preposições podem aparecer com o artigo THE: in the, on the, at the, to the. Também podem aparecer com A/AN ou com pronomes possessivos: in a room, at a school, on my desk, to my house."

    override val verbs = arrayOf(
        BookAudioItem("to work", "trabalhar, funcionar", BOOK_AUDIO_RES_ID, 5235, 7715, true),
        BookAudioItem("to have", "ter, possuir", BOOK_AUDIO_RES_ID, 7715, 10265, true),
        BookAudioItem("to go (to)", "ir (para, ao, à)", BOOK_AUDIO_RES_ID, 10265, 12865, true)
    )

    override val vocabulary = arrayOf(
        BookAudioItem("in", "na, no, em, dentro, entrada", BOOK_AUDIO_RES_ID, 15905, 18250, true),
        BookAudioItem("word", "palavra", BOOK_AUDIO_RES_ID, 34760, 37605, true),
        BookAudioItem("home", "casa, lar", BOOK_AUDIO_RES_ID, 53940, 56280, true),
        BookAudioItem("at", "na, no, em", BOOK_AUDIO_RES_ID, 18250, 20720, true),
        BookAudioItem("always", "sempre", BOOK_AUDIO_RES_ID, 37605, 40645, true),
        BookAudioItem("school", "escola, colégio", BOOK_AUDIO_RES_ID, 56280, 58875, true),
        BookAudioItem("on", "na, no, em cima, sobre", BOOK_AUDIO_RES_ID, 20720, 23395, true),
        BookAudioItem("never", "nunca", BOOK_AUDIO_RES_ID, 40645, 43340, true),
        BookAudioItem("store", "loja, mercado", BOOK_AUDIO_RES_ID, 58875, 61565, true),
        BookAudioItem("to", "para, à, ao", BOOK_AUDIO_RES_ID, 23395, 25920, true),
        BookAudioItem("book", "livro", BOOK_AUDIO_RES_ID, 43340, 46130, true),
        BookAudioItem("church", "igreja", BOOK_AUDIO_RES_ID, 61565, 64335, true),
        BookAudioItem("this", "esse, essa, este, esta", BOOK_AUDIO_RES_ID, 25920, 28910, true),
        BookAudioItem("car", "carro", BOOK_AUDIO_RES_ID, 46130, 48730, true),
        BookAudioItem("mall", "shopping center", BOOK_AUDIO_RES_ID, 64335, 66805, true),
        BookAudioItem("that", "aquele, aquela, aquilo, que", BOOK_AUDIO_RES_ID, 28910, 31835, true),
        BookAudioItem("table", "mesa", BOOK_AUDIO_RES_ID, 48730, 51270, true),
        BookAudioItem("new", "novo", BOOK_AUDIO_RES_ID, 66805, 69075, true),
        BookAudioItem("what", "o que, qual, que", BOOK_AUDIO_RES_ID, 31835, 34760, true),
        BookAudioItem("downtown", "centro da cidade", BOOK_AUDIO_RES_ID, 51270, 53940, true),
        BookAudioItem("old", "velho", BOOK_AUDIO_RES_ID, 69075, 71395, true)
    )

    override val expressions = arrayOf(
        BookAudioItem("take your time", "leve o tempo que precisar", BOOK_AUDIO_RES_ID, 74120, 76605, true),
        BookAudioItem("hey", "oi, olá", BOOK_AUDIO_RES_ID, 81525, 83780, true),
        BookAudioItem("that’s it", "é isso aí, basta", BOOK_AUDIO_RES_ID, 76605, 79050, true),
        BookAudioItem("wow", "uau, nossa!", BOOK_AUDIO_RES_ID, 83780, 86165, true),
        BookAudioItem("never mind", "esquece, deixa pra lá", BOOK_AUDIO_RES_ID, 79050, 81525, true),
        BookAudioItem("my God!", "meu Deus!", BOOK_AUDIO_RES_ID, 86165, 88990, true)
    )

    override val grammarSentences = arrayOf(
        BookAudioItem("I work downtown.", "Eu trabalho no centro.", BOOK_AUDIO_RES_ID, 91655, 94970, true),
        BookAudioItem("I work at the mall.", "Eu trabalho no shopping.", BOOK_AUDIO_RES_ID, 106960, 110100, true),
        BookAudioItem("I work (at) home.", "Eu trabalho em casa.", BOOK_AUDIO_RES_ID, 94970, 98060, true),
        BookAudioItem("I work on the table.", "Eu trabalho na mesa (em cima).", BOOK_AUDIO_RES_ID, 110100, 113320, true),
        BookAudioItem("I work at school.", "Eu trabalho na escola.", BOOK_AUDIO_RES_ID, 98060, 101060, true),
        BookAudioItem("I go home.", "Eu vou para casa.", BOOK_AUDIO_RES_ID, 113320, 115980, true),
        BookAudioItem("I work at church.", "Eu trabalho na igreja.", BOOK_AUDIO_RES_ID, 101060, 104085, true),
        BookAudioItem("I go to school.", "Eu vou para a escola.", BOOK_AUDIO_RES_ID, 115980, 118845, true),
        BookAudioItem("I work at work.", "Eu trabalho no trabalho.", BOOK_AUDIO_RES_ID, 104085, 106960, true),
        BookAudioItem("I go to the store.", "Eu vou para a loja.", BOOK_AUDIO_RES_ID, 118845, 122138, true)
    )

    override val alphabet = Lesson1BookData.alphabet
}
