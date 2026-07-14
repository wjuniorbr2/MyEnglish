package com.example.myenglish.data

import com.example.myenglish.R

object Lesson6BookData : BookLessonData {
    override val bookAudioResId = 0
    override val alphabetAudioResId = Lesson1BookData.ALPHABET_AUDIO_RES_ID
    override val title = BookAudioItem("LESSON 6", "", 0, 0, 0, true)
    override val verbsTitle = BookAudioItem("VERBS", "", 0, 0, 0, true)
    override val vocabularyTitle = BookAudioItem("VOCABULARY", "", 0, 0, 0, true)
    override val expressionsTitle = BookAudioItem("EXPRESSIONS", "", 0, 0, 0, true)
    override val grammarTitle = BookAudioItem("GRAMMAR", "", 0, 0, 0, true)
    override val alphabetTitle = Lesson1BookData.alphabetTitle
    override val grammarNoteText = "Contável e incontável referem-se a substantivos. Many é usado com contáveis; much com incontáveis; a lot of/lots of com ambos. Very é usado para intensidade/adjetivos."
    override val grammarInfoText = grammarNoteText
    override val verbs = arrayOf(
        BookAudioItem("to need", "precisar de", 0, 0, 0, true),
        BookAudioItem("to learn", "aprender", 0, 0, 0, true),
        BookAudioItem("to understand", "entender", 0, 0, 0, true),
        BookAudioItem("to prefer", "preferir", 0, 0, 0, true))
    override val vocabulary = arrayOf(
        BookAudioItem("than", "do que", 0, 0, 0, true),
        BookAudioItem("plate", "prato, placa", 0, 0, 0, true),
        BookAudioItem("egg", "ovo", 0, 0, 0, true),
        BookAudioItem("screen", "tela", 0, 0, 0, true),
        BookAudioItem("pretty", "bonito(a), lindo(a), muito", 0, 0, 0, true),
        BookAudioItem("rice", "arroz", 0, 0, 0, true),
        BookAudioItem("ball", "bola", 0, 0, 0, true),
        BookAudioItem("beautiful", "bonito(a), lindo(a)", 0, 0, 0, true),
        BookAudioItem("beans", "feijão", 0, 0, 0, true),
        BookAudioItem("file", "arquivo", 0, 0, 0, true),
        BookAudioItem("handsome", "bonito (homem)", 0, 0, 0, true),
        BookAudioItem("ice", "gelo", 0, 0, 0, true),
        BookAudioItem("of", "de", 0, 0, 0, true),
        BookAudioItem("left", "esquerdo", 0, 0, 0, true),
        BookAudioItem("tea", "chá", 0, 0, 0, true),
        BookAudioItem("glass", "vidro, copo de vidro", 0, 0, 0, true),
        BookAudioItem("horrible", "horrível", 0, 0, 0, true),
        BookAudioItem("coffee", "café", 0, 0, 0, true),
        BookAudioItem("cup", "xícara, copo de plástico", 0, 0, 0, true),
        BookAudioItem("wind", "vento", 0, 0, 0, true),
        BookAudioItem("rain", "chuva, chover", 0, 0, 0, true)
    )
    override val expressions = arrayOf(
        BookAudioItem("many", "muitos(as), contável", 0, 0, 0, true),
        BookAudioItem("How many", "quantos(as), contável", 0, 0, 0, true),
        BookAudioItem("much", "muito(a), incontável", 0, 0, 0, true),
        BookAudioItem("How much", "quanto(a), incontável", 0, 0, 0, true),
        BookAudioItem("a lot of", "muito(a), muitos(as)", 0, 0, 0, true),
        BookAudioItem("very much / a lot", "muito, intensidade", 0, 0, 0, true),
        BookAudioItem("lots of", "muito(a), muitos(as)", 0, 0, 0, true),
        BookAudioItem("very", "muito, adjetivos", 0, 0, 0, true))
    override val grammarSentences = arrayOf(
        BookAudioItem("I have many cars.", "Eu tenho muitos carros.", 0, 0, 0, true),
        BookAudioItem("I have much rice.", "Eu tenho muito arroz.", 0, 0, 0, true),
        BookAudioItem("I have a lot of cars.", "Eu tenho muitos carros.", 0, 0, 0, true),
        BookAudioItem("I have a lot of rice.", "Eu tenho muito arroz.", 0, 0, 0, true),
        BookAudioItem("I like my mother very much.", "Eu gosto muito da minha mãe.", 0, 0, 0, true),
        BookAudioItem("I study a lot.", "Eu estudo muito.", 0, 0, 0, true),
        BookAudioItem("I need a very good house.", "Eu preciso de uma casa muito boa.", 0, 0, 0, true),
        BookAudioItem("The file works pretty well.", "O arquivo funciona muito bem.", 0, 0, 0, true))
    override val alphabet = Lesson1BookData.alphabet
}

object Lesson6ListeningData {
    val AUDIO_RES_ID = R.raw.lesson6
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "We need rain this month.", 0, 4354),
        HomeworkSentence("Sentence 2", "How much water do you drink at night?", 4354, 9685),
        HomeworkSentence("Sentence 3", "It spells R-A-I-N.", 9685, 15371),
        HomeworkSentence("Sentence 4", "I eat lots of things in the mall.", 15371, 20880),
        HomeworkSentence("Sentence 5", "He prefers eggs to ham.", 20880, 26389),
        HomeworkSentence("Sentence 6", "Does she always drink a lot of coffee in the morning?", 26389, 32963),
        HomeworkSentence("Sentence 7", "Excuse me, do you learn French in this school?", 32963, 39894),
        HomeworkSentence("Sentence 8", "Where do you need to go with your friend now?", 39894, 46291),
        HomeworkSentence("Sentence 9", "My friend and I need to go to church to understand this.", 46291, 53754),
        HomeworkSentence("Sentence 10", "My mom always goes downtown to work.", 53754, 59352),
        HomeworkSentence("Sentence 11", "Do they need to learn science?", 59352, 63972),
        HomeworkSentence("Sentence 12", "They don’t need but they want to.", 63972, 69214),
        HomeworkSentence("Sentence 13", "What plate does he want today?", 69214, 74812),
        HomeworkSentence("Sentence 14", "I always drink a cup of coffee in the morning.", 74812, 81387),
        HomeworkSentence("Sentence 15", "For me coffee works ok.", 81387, 87340),
        HomeworkSentence("Sentence 16", "Rice, beans, eggs and meat, that’s it.", 87340, 94892),
        HomeworkSentence("Sentence 17", "Never mind, I prefer to learn English.", 94892, 102533),
        HomeworkSentence("Sentence 18", "What does he prefer, wine or beer?", 102533, 108575),
        HomeworkSentence("Sentence 19", "She likes wine very much.", 108575, 113817),
        HomeworkSentence("Sentence 20", "Does he prefer to study here or in your house?", 113817, 119770),
        HomeworkSentence("Sentence 21", "What? She prefers beer to water?", 119770, 125545),
        HomeworkSentence("Sentence 22", "Hey, what do you have for me?", 125545, 131232),
        HomeworkSentence("Sentence 23", "We always understand you.", 131232, 136829),
        HomeworkSentence("Sentence 24", "We don’t need this now, we need that.", 136829, 143315),
        HomeworkSentence("Sentence 25", "Where does she go after she comes from school?", 143315, 149535)
    )
}

object Lesson6SpokenData {
    val sentences = lesson6SpokenSentences
}

object Lesson6WrittenData {
    val sentences = lesson6WrittenSentences
}

private val lesson6SpokenSentences = arrayOf(
        SpokenHomeworkSentence("Eu preciso aprender inglês.", "I need to learn English."),
        SpokenHomeworkSentence("Você entende este arquivo?", "Do you understand this file?"),
        SpokenHomeworkSentence("Ela prefere café ou chá?", "Does she prefer coffee or tea?"),
        SpokenHomeworkSentence("Ele precisa de arroz e feijão.", "He needs rice and beans."),
        SpokenHomeworkSentence("Quantos ovos você tem?", "How many eggs do you have?"),
        SpokenHomeworkSentence("Quanto café você quer?", "How much coffee do you want?"),
        SpokenHomeworkSentence("Eu tenho muitos pratos.", "I have many plates."),
        SpokenHomeworkSentence("Nós temos muito arroz.", "We have much rice."),
        SpokenHomeworkSentence("Eles têm muitas xícaras.", "They have many cups."),
        SpokenHomeworkSentence("Eu bebo muito chá.", "I drink a lot of tea."),
        SpokenHomeworkSentence("Ela gosta muito de café.", "She likes coffee very much."),
        SpokenHomeworkSentence("Este arquivo funciona muito bem.", "This file works very well."),
        SpokenHomeworkSentence("A tela funciona muito bem.", "The screen works very well."),
        SpokenHomeworkSentence("Ela gosta muito da bola.", "She likes the ball very much."),
        SpokenHomeworkSentence("Ele prefere este arquivo.", "He prefers this file."),
        SpokenHomeworkSentence("Ela não gosta da comida.", "She doesn’t like the food."),
        SpokenHomeworkSentence("Eu quero um copo de água com gelo.", "I want a glass of water with ice."),
        SpokenHomeworkSentence("Ela quer uma xícara de café.", "She wants a cup of coffee."),
        SpokenHomeworkSentence("Nós precisamos de chuva este mês.", "We need rain this month."),
        SpokenHomeworkSentence("Eu gosto de vento.", "I like wind."),
        SpokenHomeworkSentence("Ele prefere este prato.", "He prefers this plate."),
        SpokenHomeworkSentence("Ela aprende ciência este ano.", "She learns science this year."),
        SpokenHomeworkSentence("Você precisa entender o arquivo agora?", "Do you need to understand the file now?"),
        SpokenHomeworkSentence("Eu prefiro chá a café.", "I prefer tea to coffee.")
)

private val lesson6WrittenSentences = arrayOf(
        WrittenHomeworkSentence("Eu preciso de uma xícara.", "I need a cup."),
        WrittenHomeworkSentence("Você precisa aprender matemática.", "You need to learn math."),
        WrittenHomeworkSentence("Nós entendemos você.", "We understand you."),
        WrittenHomeworkSentence("Eles preferem arroz.", "They prefer rice."),
        WrittenHomeworkSentence("Ela prefere feijão a arroz.", "She prefers beans to rice."),
        WrittenHomeworkSentence("Ele aprende inglês hoje.", "He learns English today."),
        WrittenHomeworkSentence("Quantas bolas você quer?", "How many balls do you want?"),
        WrittenHomeworkSentence("Quantos pratos ela tem?", "How many plates does she have?"),
        WrittenHomeworkSentence("Quanto chá ele bebe?", "How much tea does he drink?"),
        WrittenHomeworkSentence("Quanto gelo você quer?", "How much ice do you want?"),
        WrittenHomeworkSentence("Eu tenho muitos ovos.", "I have many eggs."),
        WrittenHomeworkSentence("Ela tem muito café.", "She has much coffee."),
        WrittenHomeworkSentence("Nós temos muitas coisas.", "We have a lot of things."),
        WrittenHomeworkSentence("Ele estuda muito.", "He studies a lot."),
        WrittenHomeworkSentence("Ela gosta muito de chá.", "She likes tea very much."),
        WrittenHomeworkSentence("A tela funciona muito bem.", "The screen works very well."),
        WrittenHomeworkSentence("Este arquivo funciona muito bem.", "This file works very well."),
        WrittenHomeworkSentence("Ela gosta muito da casa.", "She likes the house very much."),
        WrittenHomeworkSentence("O amigo prefere esta casa.", "The friend prefers this house."),
        WrittenHomeworkSentence("Ela não gosta da comida.", "She doesn’t like the food."),
        WrittenHomeworkSentence("Eu quero um copo de vidro.", "I want a glass."),
        WrittenHomeworkSentence("Ele quer café com gelo.", "He wants coffee with ice."),
        WrittenHomeworkSentence("Nós precisamos de chuva agora.", "We need rain now."),
        WrittenHomeworkSentence("Ela entende este número mais do que ele.", "She understands this number more than he does.")
)
