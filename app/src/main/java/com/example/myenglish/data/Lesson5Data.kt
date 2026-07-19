package com.example.myenglish.data

import com.example.myenglish.R

object Lesson5BookData : BookLessonData {
    override val bookAudioResId = 0
    override val alphabetAudioResId = Lesson1BookData.ALPHABET_AUDIO_RES_ID
    override val title = BookAudioItem("LESSON 5", "", 0, 0, 0, true)
    override val verbsTitle = BookAudioItem("VERBS", "", 0, 0, 0, true)
    override val vocabularyTitle = BookAudioItem("VOCABULARY", "", 0, 0, 0, true)
    override val expressionsTitle = BookAudioItem("EXPRESSIONS", "", 0, 0, 0, true)
    override val grammarTitle = BookAudioItem("GRAMMAR", "", 0, 0, 0, true)
    override val alphabetTitle = Lesson1BookData.alphabetTitle
    override val grammarNoteText = "Os sujeitos he, she e it usam DOES no presente."
    override val grammarInfoText = "O DOES é usado com he, she e it em perguntas e frases negativas. Para I, you, we e they, usamos DO."
    override val verbs = arrayOf(
        BookAudioItem("to start", "começar, ligar", 0, 0, 0, true),
        BookAudioItem("to finish", "terminar", 0, 0, 0, true),
        BookAudioItem("to open", "abrir", 0, 0, 0, true),
        BookAudioItem("to close", "fechar", 0, 0, 0, true))
    override val vocabulary = arrayOf(
        BookAudioItem("yesterday", "ontem", 0, 0, 0, true),
        BookAudioItem("year", "ano", 0, 0, 0, true),
        BookAudioItem("ok", "ok, beleza, sim", 0, 0, 0, true),
        BookAudioItem("today", "hoje", 0, 0, 0, true),
        BookAudioItem("he", "ele", 0, 0, 0, true),
        BookAudioItem("number", "número", 0, 0, 0, true),
        BookAudioItem("tomorrow", "amanhã", 0, 0, 0, true),
        BookAudioItem("she", "ela", 0, 0, 0, true),
        BookAudioItem("thing", "coisa", 0, 0, 0, true),
        BookAudioItem("after", "depois", 0, 0, 0, true),
        BookAudioItem("it", "ele, ela, neutro", 0, 0, 0, true),
        BookAudioItem("where", "onde", 0, 0, 0, true),
        BookAudioItem("before", "antes", 0, 0, 0, true),
        BookAudioItem("more", "mais", 0, 0, 0, true),
        BookAudioItem("class", "aula, sala", 0, 0, 0, true),
        BookAudioItem("now", "agora", 0, 0, 0, true),
        BookAudioItem("less", "menos", 0, 0, 0, true),
        BookAudioItem("door", "porta", 0, 0, 0, true),
        BookAudioItem("day", "dia", 0, 0, 0, true),
        BookAudioItem("more or less", "mais ou menos", 0, 0, 0, true),
        BookAudioItem("window", "janela", 0, 0, 0, true),
        BookAudioItem("month", "mês", 0, 0, 0, true)
    )
    override val expressions = arrayOf(
        BookAudioItem("Don’t worry", "não se preocupe", 0, 0, 0, true),
        BookAudioItem("Please", "por favor", 0, 0, 0, true),
        BookAudioItem("Cool", "legal, massa", 0, 0, 0, true),
        BookAudioItem("Sorry", "desculpe", 0, 0, 0, true),
        BookAudioItem("Sometimes", "às vezes", 0, 0, 0, true),
        BookAudioItem("Excuse me", "com licença, desculpe", 0, 0, 0, true))
    override val grammarSentences = arrayOf(
        BookAudioItem("She starts the class.", "Ela começa a aula.", 0, 0, 0, true),
        BookAudioItem("He doesn’t finish the food.", "Ele não termina a comida.", 0, 0, 0, true),
        BookAudioItem("Does she open this door?", "Ela abre esta porta?", 0, 0, 0, true),
        BookAudioItem("Doesn’t she close that window?", "Ela não fecha aquela janela?", 0, 0, 0, true),
        BookAudioItem("It eats the food.", "Ele come a comida.", 0, 0, 0, true)
    )
    override val alphabet = Lesson1BookData.alphabet
}

object Lesson5ListeningData {
    val AUDIO_RES_ID = R.raw.lesson5
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "Where do you eat today?", 0, 4193),
        HomeworkSentence("Sentence 2", "She wants to start the work.", 4193, 9365),
        HomeworkSentence("Sentence 3", "He doesn’t want to finish the class now.", 9365, 15654),
        HomeworkSentence("Sentence 4", "Where does she go at night?", 15654, 21245),
        HomeworkSentence("Sentence 5", "Does she like this?", 21245, 26277),
        HomeworkSentence("Sentence 6", "Cool, I have thirteen days to finish.", 26277, 32287),
        HomeworkSentence("Sentence 7", "Does it like the food?", 32287, 37179),
        HomeworkSentence("Sentence 8", "We have to study tomorrow morning.", 37179, 43050),
        HomeworkSentence("Sentence 9", "I have to eat before my class today.", 43050, 49200),
        HomeworkSentence("Sentence 10", "She wants to eat eighteen apples.", 49200, 54931),
        HomeworkSentence("Sentence 11", "They don’t have to finish twelve books.", 54931, 60521),
        HomeworkSentence("Sentence 12", "How are you today? I’m cool, thanks.", 60521, 66811),
        HomeworkSentence("Sentence 13", "What number does he want?", 66811, 72542),
        HomeworkSentence("Sentence 14", "We like to open the windows in the morning.", 72542, 78552),
        HomeworkSentence("Sentence 15", "Where does he study science?", 78552, 84422),
        HomeworkSentence("Sentence 16", "He has to work two months.", 84422, 90293),
        HomeworkSentence("Sentence 17", "Do you have to work after your class today?", 90700, 97002),
        HomeworkSentence("Sentence 18", "Sometimes I speak Spanish, but I always speak English.", 97002, 104800),
        HomeworkSentence("Sentence 19", "What things do you like to eat at home?", 104500, 110420),
        HomeworkSentence("Sentence 20", "My window doesn’t open today.", 110420, 115731),
        HomeworkSentence("Sentence 21", "Sorry, I don’t speak German.", 115731, 121462),
        HomeworkSentence("Sentence 22", "He eats hot things.", 121462, 127053),
        HomeworkSentence("Sentence 23", "What door do you wanna open now?", 127053, 133483),
        HomeworkSentence("Sentence 24", "What year do you have to go?", 133483, 139213),
        HomeworkSentence("Sentence 25", "She wants more apples and less bread.", 139213, 146202),
        HomeworkSentence("Sentence 26", "Excuse me, I have to come.", 146202, 152072),
        HomeworkSentence("Sentence 27", "Don’t worry, drink cold water and go.", 152072, 159150),
        HomeworkSentence("Sentence 28", "That’s it, you have to open thirteen doors.", 158642, 165351),
        HomeworkSentence("Sentence 29", "What days do you work this month?", 165351, 171640),
        HomeworkSentence("Sentence 30", "Where does she go at night?", 171640, 177091),
        HomeworkSentence("Sentence 31", "Please open your books and start the homework now.", 177091, 184360),
        HomeworkSentence("Sentence 32", "He starts things, but he doesn’t finish.", 184360, 190649),
        HomeworkSentence("Sentence 33", "Excuse me, do you speak French?", 190649, 196660),
        HomeworkSentence("Sentence 34", "I have to study in the morning, work in the afternoon and read at night.", 196660, 205046),
        HomeworkSentence("Sentence 35", "What month do we have to study?", 205046, 211475),
        HomeworkSentence("Sentence 36", "You help me today and I help you tomorrow, ok?", 211475, 218744),
        HomeworkSentence("Sentence 37", "I study before my class, but I eat before I study.", 218744, 225592),
        HomeworkSentence("Sentence 38", "He studies where you work.", 226300, 232600),
        HomeworkSentence("Sentence 39", "Help me, help you.", 231323, 237054)
    )
}

object Lesson5SpokenData {
    val sentences = lesson5SpokenSentences
}

object Lesson5WrittenData {
    val sentences = lesson5WrittenSentences
}

private val lesson5SpokenSentences = arrayOf(
        SpokenHomeworkSentence("Ela começa a aula hoje.", "She starts the class today."),
        SpokenHomeworkSentence("Ele não termina a comida agora.", "He doesn’t finish the food now."),
        SpokenHomeworkSentence("Ela abre a porta?", "Does she open the door?"),
        SpokenHomeworkSentence("Ele fecha a janela.", "He closes the window."),
        SpokenHomeworkSentence("Onde ela estuda?", "Where does she study?"),
        SpokenHomeworkSentence("Ele começa antes da aula.", "He starts before class."),
        SpokenHomeworkSentence("Ela termina depois do trabalho.", "She finishes after work."),
        SpokenHomeworkSentence("Ele quer mais comida.", "He wants more food."),
        SpokenHomeworkSentence("Ela quer menos pão.", "She wants less bread."),
        SpokenHomeworkSentence("Que número ele quer?", "What number does he want?"),
        SpokenHomeworkSentence("Ela não abre esta janela.", "She doesn’t open this window."),
        SpokenHomeworkSentence("Ele não fecha aquela porta.", "He doesn’t close that door."),
        SpokenHomeworkSentence("Ele (o cachorro) come a comida.", "It eats the food."),
        SpokenHomeworkSentence("Hoje ele abre a porta.", "He opens the door today."),
        SpokenHomeworkSentence("Amanhã ele começa o trabalho.", "Tomorrow he starts the work."),
        SpokenHomeworkSentence("Ontem ela terminou a aula.", "Yesterday she finished the class."),
        SpokenHomeworkSentence("Por favor, abra a porta.", "Please open the door."),
        SpokenHomeworkSentence("Por favor, feche a janela.", "Please close the window."),
        SpokenHomeworkSentence("Desculpe, eu não entendo.", "Sorry, I don’t understand."),
        SpokenHomeworkSentence("Com licença, onde ele trabalha?", "Excuse me, where does he work?"),
        SpokenHomeworkSentence("Não se preocupe, ele termina hoje.", "Don’t worry, he finishes today."),
        SpokenHomeworkSentence("Legal, ela começa agora.", "Cool, she starts now."),
        SpokenHomeworkSentence("Às vezes ele fecha a loja.", "Sometimes he closes the store."),
        SpokenHomeworkSentence("Em que mês ela começa?", "What month does she start?")
)

private val lesson5WrittenSentences = arrayOf(
        WrittenHomeworkSentence("Ele abre a porta agora.", "He opens the door now."),
        WrittenHomeworkSentence("Ela fecha a janela hoje.", "She closes the window today."),
        WrittenHomeworkSentence("Ela começa a aula amanhã.", "She starts the class tomorrow."),
        WrittenHomeworkSentence("Ele termina o trabalho depois da aula.", "He finishes the work after class."),
        WrittenHomeworkSentence("Ela não termina o livro.", "She doesn’t finish the book."),
        WrittenHomeworkSentence("Ele não abre esta porta.", "He doesn’t open this door."),
        WrittenHomeworkSentence("Ela fecha aquela janela?", "Does she close that window?"),
        WrittenHomeworkSentence("Ele começa hoje?", "Does he start today?"),
        WrittenHomeworkSentence("Onde ela trabalha?", "Where does she work?"),
        WrittenHomeworkSentence("Onde ele come hoje?", "Where does he eat today?"),
        WrittenHomeworkSentence("Que dia ele começa?", "What day does he start?"),
        WrittenHomeworkSentence("Que ano ela termina?", "What year does she finish?"),
        WrittenHomeworkSentence("Ele quer mais coisas.", "He wants more things."),
        WrittenHomeworkSentence("Ela quer menos comida.", "She wants less food."),
        WrittenHomeworkSentence("Ela (a porta) não abre.", "It doesn’t open."),
        WrittenHomeworkSentence("Ela (a aula) começa agora.", "It starts now."),
        WrittenHomeworkSentence("Por favor, comece agora.", "Please start now."),
        WrittenHomeworkSentence("Por favor, termine hoje.", "Please finish today."),
        WrittenHomeworkSentence("Desculpe, ela não fala inglês.", "Sorry, she doesn’t speak English."),
        WrittenHomeworkSentence("Com licença, ele trabalha aqui?", "Excuse me, does he work here?"),
        WrittenHomeworkSentence("Não se preocupe, ela fecha a loja.", "Don’t worry, she closes the store."),
        WrittenHomeworkSentence("Legal, ele abre a janela.", "Cool, he opens the window."),
        WrittenHomeworkSentence("Às vezes ela estuda antes do trabalho.", "Sometimes she studies before work."),
        WrittenHomeworkSentence("Ela termina mais ou menos hoje.", "She finishes more or less today.")
)
