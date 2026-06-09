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
        HomeworkSentence("Sentence 1", "We need rain this month.", 470, 14500),
        HomeworkSentence("Sentence 2", "How much water do you drink at night?", 15780, 20370),
        HomeworkSentence("Sentence 3", "It spells R-A-I-N.", 21680, 30780),
        HomeworkSentence("Sentence 4", "I eat lots of things in the mall.", 32790, 37910),
        HomeworkSentence("Sentence 5", "He prefers eggs than ham.", 39190, 40130),
        HomeworkSentence("Sentence 6", "Does she always drink a lot of coffee in the morning?", 41500, 44710),
        HomeworkSentence("Sentence 7", "Excuse me, do you learn French in this school?", 46130, 50920),
        HomeworkSentence("Sentence 8", "Where do you need to go with your friend now?", 52480, 58420),
        HomeworkSentence("Sentence 9", "My friend and I need to go to church to understand this.", 60000, 73700),
        HomeworkSentence("Sentence 10", "My mom always goes downtown to work.", 75510, 79480),
        HomeworkSentence("Sentence 11", "Do they need to learn science?", 80800, 86500),
        HomeworkSentence("Sentence 12", "They don’t need but they want to.", 87700, 93000),
        HomeworkSentence("Sentence 13", "What plate does he want today?", 94200, 99500),
        HomeworkSentence("Sentence 14", "I always drink a cup of coffee in the morning.", 100700, 106000),
        HomeworkSentence("Sentence 15", "For me coffee works ok.", 107200, 112000),
        HomeworkSentence("Sentence 16", "Rice, beans, eggs and meat, that’s it.", 113200, 118500),
        HomeworkSentence("Sentence 17", "Never mind, I prefer to learn English.", 119700, 125000),
        HomeworkSentence("Sentence 18", "What does he prefer, wine or beer?", 126200, 131000),
        HomeworkSentence("Sentence 19", "She likes wine very much.", 131920, 135620),
        HomeworkSentence("Sentence 20", "Does he prefer to study here or in your house?", 137460, 138510),
        HomeworkSentence("Sentence 21", "What? She prefers beer than water?", 139760, 143960),
        HomeworkSentence("Sentence 22", "Hey, what do you have for me?", 145290, 147780),
        HomeworkSentence("Sentence 23", "We always understand you.", 149550, 154100),
        HomeworkSentence("Sentence 24", "We don’t need this now, we need that.", 154100, 155200),
        HomeworkSentence("Sentence 25", "Where does she go after she comes from school?", 155200, 156756)
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
        SpokenHomeworkSentence("Este arquivo é muito bom.", "This file is very good."),
        SpokenHomeworkSentence("A tela é muito bonita.", "The screen is very pretty."),
        SpokenHomeworkSentence("A bola é bonita.", "The ball is beautiful."),
        SpokenHomeworkSentence("Ele é bonito.", "He is handsome."),
        SpokenHomeworkSentence("A comida é horrível.", "The food is horrible."),
        SpokenHomeworkSentence("Eu quero um copo de água com gelo.", "I want a glass of water with ice."),
        SpokenHomeworkSentence("Ela quer uma xícara de café.", "She wants a cup of coffee."),
        SpokenHomeworkSentence("Nós precisamos de chuva este mês.", "We need rain this month."),
        SpokenHomeworkSentence("Eu gosto de vento.", "I like wind."),
        SpokenHomeworkSentence("Ele prefere este prato.", "He prefers this plate."),
        SpokenHomeworkSentence("Ela aprende ciência este ano.", "She learns science this year."),
        SpokenHomeworkSentence("Você precisa entender isso agora?", "Do you need to understand this now?"),
        SpokenHomeworkSentence("Eu prefiro chá a café.", "I prefer tea to coffee.")
)

private val lesson6WrittenSentences = arrayOf(
        WrittenHomeworkSentence("Eu preciso de uma xícara.", "I need a cup."),
        WrittenHomeworkSentence("Você precisa aprender isso.", "You need to learn this."),
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
        WrittenHomeworkSentence("A tela é muito boa.", "The screen is very good."),
        WrittenHomeworkSentence("Este arquivo é muito pequeno.", "This file is very small."),
        WrittenHomeworkSentence("A casa é muito bonita.", "The house is very beautiful."),
        WrittenHomeworkSentence("O amigo é bonito.", "The friend is handsome."),
        WrittenHomeworkSentence("A ideia é horrível.", "The idea is horrible."),
        WrittenHomeworkSentence("Eu quero um copo de vidro.", "I want a glass."),
        WrittenHomeworkSentence("Ele quer café com gelo.", "He wants coffee with ice."),
        WrittenHomeworkSentence("Nós precisamos de chuva agora.", "We need rain now."),
        WrittenHomeworkSentence("Ela entende este número mais do que ele.", "She understands this number more than he does.")
)
