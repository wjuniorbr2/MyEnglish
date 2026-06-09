package com.example.myenglish.data

import com.example.myenglish.R

object Lesson4BookData : BookLessonData {
    override val bookAudioResId = 0
    override val alphabetAudioResId = Lesson1BookData.ALPHABET_AUDIO_RES_ID
    override val title = BookAudioItem("LESSON 4", "", 0, 0, 0, true)
    override val verbsTitle = BookAudioItem("VERBS", "", 0, 0, 0, true)
    override val vocabularyTitle = BookAudioItem("VOCABULARY", "", 0, 0, 0, true)
    override val expressionsTitle = BookAudioItem("EXPRESSIONS", "", 0, 0, 0, true)
    override val grammarTitle = BookAudioItem("GRAMMAR", "", 0, 0, 0, true)
    override val alphabetTitle = Lesson1BookData.alphabetTitle
    override val grammarNoteText = "Obs.: Em perguntas, o How vem no início da frase."
    override val grammarInfoText = "A e AN têm a mesma tradução: um, uma. São usados dependendo da primeira letra da próxima palavra. A é usado antes de som de consoante. AN é usado antes de som de vogal."
    override val verbs = arrayOf(
        BookAudioItem("to come (to)", "vir (para, à, ao)", 0, 0, 0, true),
        BookAudioItem("to write", "escrever", 0, 0, 0, true),
        BookAudioItem("to read", "ler", 0, 0, 0, true)
    )
    override val vocabulary = arrayOf(
        BookAudioItem("how", "como", 0, 0, 0, true),
        BookAudioItem("about", "sobre, aprox., mais ou menos", 0, 0, 0, true),
        BookAudioItem("country", "país, terra, interior", 0, 0, 0, true),
        BookAudioItem("person", "pessoa", 0, 0, 0, true),
        BookAudioItem("science", "ciência(s)", 0, 0, 0, true),
        BookAudioItem("state", "estado", 0, 0, 0, true),
        BookAudioItem("people", "pessoas, povo", 0, 0, 0, true),
        BookAudioItem("food", "comida, alimento", 0, 0, 0, true),
        BookAudioItem("idea", "ideia", 0, 0, 0, true),
        BookAudioItem("for", "por, para", 0, 0, 0, true),
        BookAudioItem("friend", "amigo(a)", 0, 0, 0, true),
        BookAudioItem("apple", "maçã", 0, 0, 0, true),
        BookAudioItem("from", "de origem", 0, 0, 0, true),
        BookAudioItem("story", "conto, estória", 0, 0, 0, true),
        BookAudioItem("room", "sala, quarto", 0, 0, 0, true),
        BookAudioItem("to", "para, à, ao", 0, 0, 0, true),
        BookAudioItem("history", "história", 0, 0, 0, true),
        BookAudioItem("example", "exemplo", 0, 0, 0, true),
        BookAudioItem("life", "vida", 0, 0, 0, true),
        BookAudioItem("city", "cidade", 0, 0, 0, true),
        BookAudioItem("letter", "carta, letra", 0, 0, 0, true)
    )
    override val expressions = arrayOf(
        BookAudioItem("thank you / thanks", "obrigado", 0, 0, 0, true),
        BookAudioItem("how are you?", "como você está?", 0, 0, 0, true),
        BookAudioItem("you’re welcome", "de nada", 0, 0, 0, true),
        BookAudioItem("I’m fine, thanks", "estou bem, obrigado", 0, 0, 0, true),
        BookAudioItem("good idea", "boa ideia", 0, 0, 0, true),
        BookAudioItem("I’m not well", "não estou bem", 0, 0, 0, true),
        BookAudioItem("How do you spell TESTE?", "Como você soletra TESTE?", 0, 0, 0, true),
        BookAudioItem("It spells T-E-S-T-E.", "Soletra-se T-E-S-T-E.", 0, 0, 0, true)
    )
    override val grammarSentences = arrayOf(
        BookAudioItem("I write in a store.", "Eu escrevo em uma loja.", 0, 0, 0, true),
        BookAudioItem("You read an English book.", "Você lê um livro de inglês.", 0, 0, 0, true),
        BookAudioItem("I write in an old store.", "Eu escrevo em uma loja velha.", 0, 0, 0, true),
        BookAudioItem("I don’t come to the city.", "Eu não venho para a cidade.", 0, 0, 0, true),
        BookAudioItem("They read on a table.", "Eles leem em uma mesa.", 0, 0, 0, true),
        BookAudioItem("I come to school.", "Eu venho para a escola.", 0, 0, 0, true),
        BookAudioItem("They read an idea.", "Eles leem uma ideia.", 0, 0, 0, true),
        BookAudioItem("Do you always come here?", "Você sempre vem aqui?", 0, 0, 0, true),
        BookAudioItem("You read a book.", "Você lê um livro.", 0, 0, 0, true),
        BookAudioItem("We come home.", "Nós vimos para casa.", 0, 0, 0, true)
    )
    override val alphabet = Lesson1BookData.alphabet
}

object Lesson4ListeningData {
    val AUDIO_RES_ID = R.raw.lesson4
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "Do you have an apple?", 0, 4584),
        HomeworkSentence("Sentence 2", "I wanna come home in the afternoon.", 4584, 10124),
        HomeworkSentence("Sentence 3", "We like to come to the mall at night.", 10124, 16236),
        HomeworkSentence("Sentence 4", "Do you like to write letters?", 16236, 22093),
        HomeworkSentence("Sentence 5", "Don’t you read a book in the morning?", 22093, 27824),
        HomeworkSentence("Sentence 6", "They always have good ideas.", 27824, 34191),
        HomeworkSentence("Sentence 7", "I never read about science.", 34191, 39794),
        HomeworkSentence("Sentence 8", "Don’t I have to come?", 39794, 45460),
        HomeworkSentence("Sentence 9", "Please, come home in the evening.", 45460, 51827),
        HomeworkSentence("Sentence 10", "I have to go, see ya.", 51827, 57112),
        HomeworkSentence("Sentence 11", "I don’t have to come to the store at night.", 57112, 63606),
        HomeworkSentence("Sentence 12", "I don’t write about science, but I write about countries.", 63606, 72138),
        HomeworkSentence("Sentence 13", "Please, help me with my homework.", 72138, 78632),
        HomeworkSentence("Sentence 14", "Yeah, I have beer for you, you’re welcome.", 78632, 85763),
        HomeworkSentence("Sentence 15", "Don’t we have to go to church in the evening?", 85763, 92640),
        HomeworkSentence("Sentence 16", "Do you have a good example for me?", 92640, 99070),
        HomeworkSentence("Sentence 17", "We wanna go to that city.", 99070, 105437),
        HomeworkSentence("Sentence 18", "Do they come to my state?", 105437, 111995),
        HomeworkSentence("Sentence 19", "Do they have good friends?", 111995, 117280),
        HomeworkSentence("Sentence 20", "I always write but I never read.", 117280, 123328),
        HomeworkSentence("Sentence 21", "How do you write a good story?", 123328, 128677),
        HomeworkSentence("Sentence 22", "I never read bad books, but I like good books.", 128677, 136126),
        HomeworkSentence("Sentence 23", "My friends have to come to my city.", 136126, 142620),
        HomeworkSentence("Sentence 24", "We have to eat bread with butter and hot cheese. Hmm Good idea.", 142620, 152171),
        HomeworkSentence("Sentence 25", "Do you like the life you have?", 152171, 157838),
        HomeworkSentence("Sentence 26", "What do you read in a cold morning?", 157838, 164204),
        HomeworkSentence("Sentence 27", "I like to drink cold water in hot afternoons.", 164204, 171081),
        HomeworkSentence("Sentence 28", "Never mind, they don’t wanna read this book.", 171081, 176684),
        HomeworkSentence("Sentence 29", "Don’t I always write to you?", 176684, 182541),
        HomeworkSentence("Sentence 30", "We don’t have to eat on this table, we like that table.", 182541, 189609),
        HomeworkSentence("Sentence 31", "Do they have to write a small or a big letter?", 189609, 196549),
        HomeworkSentence("Sentence 32", "Do you wanna come to my house or go to the city?", 196549, 203043),
        HomeworkSentence("Sentence 33", "How do you want the room, cold or hot?", 203043, 210174),
        HomeworkSentence("Sentence 34", "They help you with that example.", 210174, 214949)
    )
}

object Lesson4SpokenData {
    val sentences = lesson4SpokenSentences
}

object Lesson4WrittenData {
    val sentences = lesson4WrittenSentences
}

private val lesson4SpokenSentences = arrayOf(
        SpokenHomeworkSentence("Eu escrevo cartas.", "I write letters."),
        SpokenHomeworkSentence("Você lê sobre ciência?", "Do you read about science?"),
        SpokenHomeworkSentence("Nós vimos para a cidade.", "We come to the city."),
        SpokenHomeworkSentence("Eles escrevem uma boa história.", "They write a good story."),
        SpokenHomeworkSentence("Eu tenho uma ideia.", "I have an idea."),
        SpokenHomeworkSentence("Boa ideia.", "Good idea."),
        SpokenHomeworkSentence("Como você soletra apple?", "How do you spell apple?"),
        SpokenHomeworkSentence("Soletra-se A-P-P-L-E.", "It spells A-P-P-L-E."),
        SpokenHomeworkSentence("Obrigado.", "Thank you."),
        SpokenHomeworkSentence("De nada.", "You’re welcome."),
        SpokenHomeworkSentence("Como você está?", "How are you?"),
        SpokenHomeworkSentence("Estou bem, obrigado.", "I’m fine, thanks."),
        SpokenHomeworkSentence("Eu não estou bem.", "I’m not well."),
        SpokenHomeworkSentence("Você vem do país?", "Do you come from the country?"),
        SpokenHomeworkSentence("Eu escrevo sobre pessoas.", "I write about people."),
        SpokenHomeworkSentence("Eles leem sobre história.", "They read about history."),
        SpokenHomeworkSentence("Você tem um exemplo?", "Do you have an example?"),
        SpokenHomeworkSentence("Eu leio em uma sala.", "I read in a room."),
        SpokenHomeworkSentence("Nós escrevemos para amigos.", "We write to friends."),
        SpokenHomeworkSentence("Eles vêm para a minha cidade.", "They come to my city."),
        SpokenHomeworkSentence("Eu quero ler uma carta.", "I want to read a letter."),
        SpokenHomeworkSentence("Você quer escrever uma história?", "Do you want to write a story?"),
        SpokenHomeworkSentence("Eu gosto de ler sobre vida.", "I like to read about life."),
        SpokenHomeworkSentence("As pessoas vêm para a escola.", "People come to school.")
)

private val lesson4WrittenSentences = arrayOf(
        WrittenHomeworkSentence("Eu venho para a cidade.", "I come to the city."),
        WrittenHomeworkSentence("Você escreve uma carta?", "Do you write a letter?"),
        WrittenHomeworkSentence("Eles leem um livro de história.", "They read a history book."),
        WrittenHomeworkSentence("Nós escrevemos sobre ciência.", "We write about science."),
        WrittenHomeworkSentence("Eu tenho uma maçã.", "I have an apple."),
        WrittenHomeworkSentence("Você tem uma boa ideia.", "Do you have a good idea?"),
        WrittenHomeworkSentence("Como você escreve story?", "How do you write story?"),
        WrittenHomeworkSentence("Como você soletra letter?", "How do you spell letter?"),
        WrittenHomeworkSentence("Soletra-se L-E-T-T-E-R.", "It spells L-E-T-T-E-R."),
        WrittenHomeworkSentence("Eu leio sobre países.", "I read about countries."),
        WrittenHomeworkSentence("Nós vimos do estado.", "We come from the state."),
        WrittenHomeworkSentence("Você lê sobre pessoas?", "Do you read about people?"),
        WrittenHomeworkSentence("Eu quero escrever para um amigo.", "I want to write to a friend."),
        WrittenHomeworkSentence("Eles não leem cartas.", "They don’t read letters."),
        WrittenHomeworkSentence("Eu não venho para esta sala.", "I don’t come to this room."),
        WrittenHomeworkSentence("Obrigado pela comida.", "Thank you for the food."),
        WrittenHomeworkSentence("Estou bem, obrigado.", "I’m fine, thanks."),
        WrittenHomeworkSentence("Não estou bem.", "I’m not well."),
        WrittenHomeworkSentence("De nada.", "You’re welcome."),
        WrittenHomeworkSentence("Boa ideia.", "Good idea."),
        WrittenHomeworkSentence("Você tem um exemplo para mim?", "Do you have an example for me?"),
        WrittenHomeworkSentence("Minha vida é boa.", "My life is good."),
        WrittenHomeworkSentence("Eles gostam da cidade.", "They like the city."),
        WrittenHomeworkSentence("Eu escrevo uma história sobre um amigo.", "I write a story about a friend.")
)
