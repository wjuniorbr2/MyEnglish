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
        BookAudioItem("how", "como", 0, 0, 0, true), BookAudioItem("about", "sobre, aprox., mais ou menos", 0, 0, 0, true), BookAudioItem("country", "país, terra, interior", 0, 0, 0, true),
        BookAudioItem("person", "pessoa", 0, 0, 0, true), BookAudioItem("science", "ciência(s)", 0, 0, 0, true), BookAudioItem("state", "estado", 0, 0, 0, true),
        BookAudioItem("people", "pessoas, povo", 0, 0, 0, true), BookAudioItem("food", "comida, alimento", 0, 0, 0, true), BookAudioItem("idea", "ideia", 0, 0, 0, true),
        BookAudioItem("for", "por, para", 0, 0, 0, true), BookAudioItem("friend", "amigo(a)", 0, 0, 0, true), BookAudioItem("apple", "maçã", 0, 0, 0, true),
        BookAudioItem("from", "de origem", 0, 0, 0, true), BookAudioItem("story", "conto, estória", 0, 0, 0, true), BookAudioItem("room", "sala, quarto", 0, 0, 0, true),
        BookAudioItem("to", "para, à, ao", 0, 0, 0, true), BookAudioItem("history", "história", 0, 0, 0, true), BookAudioItem("example", "exemplo", 0, 0, 0, true),
        BookAudioItem("life", "vida", 0, 0, 0, true), BookAudioItem("city", "cidade", 0, 0, 0, true), BookAudioItem("letter", "carta, letra", 0, 0, 0, true)
    )
    override val expressions = arrayOf(
        BookAudioItem("thank you / thanks", "obrigado", 0, 0, 0, true), BookAudioItem("how are you?", "como você está?", 0, 0, 0, true),
        BookAudioItem("you’re welcome", "de nada", 0, 0, 0, true), BookAudioItem("I’m fine, thanks", "estou bem, obrigado", 0, 0, 0, true),
        BookAudioItem("good idea", "boa ideia", 0, 0, 0, true), BookAudioItem("I’m not well", "não estou bem", 0, 0, 0, true),
        BookAudioItem("How do you spell TESTE?", "Como você soletra TESTE?", 0, 0, 0, true), BookAudioItem("It spells T-E-S-T-E.", "Soletra-se T-E-S-T-E.", 0, 0, 0, true)
    )
    override val grammarSentences = arrayOf(
        BookAudioItem("I write in a store.", "Eu escrevo em uma loja.", 0, 0, 0, true), BookAudioItem("You read an English book.", "Você lê um livro de inglês.", 0, 0, 0, true),
        BookAudioItem("I write in an old store.", "Eu escrevo em uma loja velha.", 0, 0, 0, true), BookAudioItem("I don’t come to the city.", "Eu não venho para a cidade.", 0, 0, 0, true),
        BookAudioItem("They read on a table.", "Eles leem em uma mesa.", 0, 0, 0, true), BookAudioItem("I come to school.", "Eu venho para a escola.", 0, 0, 0, true),
        BookAudioItem("They read an idea.", "Eles leem uma ideia.", 0, 0, 0, true), BookAudioItem("Do you always come here?", "Você sempre vem aqui?", 0, 0, 0, true),
        BookAudioItem("You read a book.", "Você lê um livro.", 0, 0, 0, true), BookAudioItem("We come home.", "Nós vimos para casa.", 0, 0, 0, true)
    )
    override val alphabet = Lesson1BookData.alphabet
}

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
    override val verbs = arrayOf(BookAudioItem("to start", "começar, ligar", 0, 0, 0, true), BookAudioItem("to finish", "terminar", 0, 0, 0, true), BookAudioItem("to open", "abrir", 0, 0, 0, true), BookAudioItem("to close", "fechar", 0, 0, 0, true))
    override val vocabulary = arrayOf(
        BookAudioItem("yesterday", "ontem", 0, 0, 0, true), BookAudioItem("year", "ano", 0, 0, 0, true), BookAudioItem("ok", "ok, beleza, sim", 0, 0, 0, true),
        BookAudioItem("today", "hoje", 0, 0, 0, true), BookAudioItem("he", "ele", 0, 0, 0, true), BookAudioItem("number", "número", 0, 0, 0, true),
        BookAudioItem("tomorrow", "amanhã", 0, 0, 0, true), BookAudioItem("she", "ela", 0, 0, 0, true), BookAudioItem("thing", "coisa", 0, 0, 0, true),
        BookAudioItem("after", "depois", 0, 0, 0, true), BookAudioItem("it", "ele, ela, neutro", 0, 0, 0, true), BookAudioItem("where", "onde", 0, 0, 0, true),
        BookAudioItem("before", "antes", 0, 0, 0, true), BookAudioItem("more", "mais", 0, 0, 0, true), BookAudioItem("class", "aula, sala", 0, 0, 0, true),
        BookAudioItem("now", "agora", 0, 0, 0, true), BookAudioItem("less", "menos", 0, 0, 0, true), BookAudioItem("door", "porta", 0, 0, 0, true),
        BookAudioItem("day", "dia", 0, 0, 0, true), BookAudioItem("more or less", "mais ou menos", 0, 0, 0, true), BookAudioItem("window", "janela", 0, 0, 0, true),
        BookAudioItem("month", "mês", 0, 0, 0, true)
    )
    override val expressions = arrayOf(BookAudioItem("Don’t worry", "não se preocupe", 0, 0, 0, true), BookAudioItem("Please", "por favor", 0, 0, 0, true), BookAudioItem("Cool", "legal, massa", 0, 0, 0, true), BookAudioItem("Sorry", "desculpe", 0, 0, 0, true), BookAudioItem("Sometimes", "às vezes", 0, 0, 0, true), BookAudioItem("Excuse me", "com licença, desculpe", 0, 0, 0, true))
    override val grammarSentences = arrayOf(
        BookAudioItem("She starts the class.", "Ela começa a aula.", 0, 0, 0, true), BookAudioItem("He doesn’t finish the food.", "Ele não termina a comida.", 0, 0, 0, true),
        BookAudioItem("Does she open this door?", "Ela abre esta porta?", 0, 0, 0, true), BookAudioItem("Doesn’t she close that window?", "Ela não fecha aquela janela?", 0, 0, 0, true),
        BookAudioItem("It eats the food.", "Ele come a comida.", 0, 0, 0, true)
    )
    override val alphabet = Lesson1BookData.alphabet
}

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
    override val verbs = arrayOf(BookAudioItem("to need", "precisar de", 0, 0, 0, true), BookAudioItem("to learn", "aprender", 0, 0, 0, true), BookAudioItem("to understand", "entender", 0, 0, 0, true), BookAudioItem("to prefer", "preferir", 0, 0, 0, true))
    override val vocabulary = arrayOf(
        BookAudioItem("than", "do que", 0, 0, 0, true), BookAudioItem("plate", "prato, placa", 0, 0, 0, true), BookAudioItem("egg", "ovo", 0, 0, 0, true),
        BookAudioItem("screen", "tela", 0, 0, 0, true), BookAudioItem("pretty", "bonito(a), lindo(a), muito", 0, 0, 0, true), BookAudioItem("rice", "arroz", 0, 0, 0, true),
        BookAudioItem("ball", "bola", 0, 0, 0, true), BookAudioItem("beautiful", "bonito(a), lindo(a)", 0, 0, 0, true), BookAudioItem("beans", "feijão", 0, 0, 0, true),
        BookAudioItem("file", "arquivo", 0, 0, 0, true), BookAudioItem("handsome", "bonito (homem)", 0, 0, 0, true), BookAudioItem("ice", "gelo", 0, 0, 0, true),
        BookAudioItem("of", "de", 0, 0, 0, true), BookAudioItem("left", "esquerdo", 0, 0, 0, true), BookAudioItem("tea", "chá", 0, 0, 0, true),
        BookAudioItem("glass", "vidro, copo de vidro", 0, 0, 0, true), BookAudioItem("horrible", "horrível", 0, 0, 0, true), BookAudioItem("coffee", "café", 0, 0, 0, true),
        BookAudioItem("cup", "xícara, copo de plástico", 0, 0, 0, true), BookAudioItem("wind", "vento", 0, 0, 0, true), BookAudioItem("rain", "chuva, chover", 0, 0, 0, true)
    )
    override val expressions = arrayOf(BookAudioItem("many", "muitos(as), contável", 0, 0, 0, true), BookAudioItem("How many", "quantos(as), contável", 0, 0, 0, true), BookAudioItem("much", "muito(a), incontável", 0, 0, 0, true), BookAudioItem("How much", "quanto(a), incontável", 0, 0, 0, true), BookAudioItem("a lot of", "muito(a), muitos(as)", 0, 0, 0, true), BookAudioItem("very much / a lot", "muito, intensidade", 0, 0, 0, true), BookAudioItem("lots of", "muito(a), muitos(as)", 0, 0, 0, true), BookAudioItem("very", "muito, adjetivos", 0, 0, 0, true))
    override val grammarSentences = arrayOf(BookAudioItem("I have many cars.", "Eu tenho muitos carros.", 0, 0, 0, true), BookAudioItem("I have much rice.", "Eu tenho muito arroz.", 0, 0, 0, true), BookAudioItem("I have a lot of cars.", "Eu tenho muitos carros.", 0, 0, 0, true), BookAudioItem("I have a lot of rice.", "Eu tenho muito arroz.", 0, 0, 0, true), BookAudioItem("I like my mother very much.", "Eu gosto muito da minha mãe.", 0, 0, 0, true), BookAudioItem("I study a lot.", "Eu estudo muito.", 0, 0, 0, true), BookAudioItem("I need a very good house.", "Eu preciso de uma casa muito boa.", 0, 0, 0, true), BookAudioItem("The file works pretty well.", "O arquivo funciona muito bem.", 0, 0, 0, true))
    override val alphabet = Lesson1BookData.alphabet
}

object Lesson4ListeningData {
    val AUDIO_RES_ID = R.raw.lesson4
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "Do you have an apple?", 610, 8540), HomeworkSentence("Sentence 2", "I wanna come home in the afternoon.", 9640, 14390), HomeworkSentence("Sentence 3", "We like to come to the mall at night.", 15470, 20430), HomeworkSentence("Sentence 4", "Do you like to write letters?", 21740, 32080), HomeworkSentence("Sentence 5", "Don’t you read a book in the morning?", 33330, 36170), HomeworkSentence("Sentence 6", "They always have good ideas.", 37520, 38470), HomeworkSentence("Sentence 7", "I never read about science.", 39530, 42050), HomeworkSentence("Sentence 8", "Don’t I have to come?", 43120, 47310), HomeworkSentence("Sentence 9", "Please, come home in the evening.", 48680, 49720), HomeworkSentence("Sentence 10", "I have to go, see ya.", 51020, 53420), HomeworkSentence("Sentence 11", "I don’t have to come to the store at night.", 54500, 58750), HomeworkSentence("Sentence 12", "I don’t write about science, but I write about countries.", 59900, 65000), HomeworkSentence("Sentence 13", "Please, help me with my homework.", 66200, 71000), HomeworkSentence("Sentence 14", "Yeah, I have beer for you, you’re welcome.", 72200, 78000), HomeworkSentence("Sentence 15", "Don’t we have to go to church in the evening?", 79200, 84800), HomeworkSentence("Sentence 16", "Do you have a good example for me?", 86000, 91000), HomeworkSentence("Sentence 17", "We wanna go to that city.", 92200, 97000), HomeworkSentence("Sentence 18", "Do they come to my state?", 98200, 103000), HomeworkSentence("Sentence 19", "Do they have good friends?", 104200, 109000), HomeworkSentence("Sentence 20", "I always write but I never read.", 110200, 115000), HomeworkSentence("Sentence 21", "How do you write a good story?", 116200, 121000), HomeworkSentence("Sentence 22", "I never read bad books, but I like good books.", 122200, 128000), HomeworkSentence("Sentence 23", "My friends have to come to my city.", 129200, 135000), HomeworkSentence("Sentence 24", "We have to eat bread with butter and hot cheese. Hmm Good idea.", 136200, 144000), HomeworkSentence("Sentence 25", "Do you like the life you have?", 145200, 150000), HomeworkSentence("Sentence 26", "What do you read in a cold morning?", 151200, 156000), HomeworkSentence("Sentence 27", "I like to drink cold water in hot afternoons.", 157200, 163000), HomeworkSentence("Sentence 28", "Never mind, they don’t wanna read this book.", 164200, 170000), HomeworkSentence("Sentence 29", "Don’t I always write to you?", 171200, 177000), HomeworkSentence("Sentence 30", "We don’t have to eat on this table, we like that table.", 185810, 191440), HomeworkSentence("Sentence 31", "Do they have to write a small or a big letter?", 192840, 194050), HomeworkSentence("Sentence 32", "Do you wanna come to my house or go to the city?", 195250, 204870), HomeworkSentence("Sentence 33", "How do you want the room, cold or hot?", 206040, 211770), HomeworkSentence("Sentence 34", "They help you with that example.", 213270, 217590)
    )
}

object Lesson5ListeningData {
    val AUDIO_RES_ID = R.raw.lesson5
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "Where do you eat today?", 0, 5480), HomeworkSentence("Sentence 2", "She wants to start the work.", 6880, 10720), HomeworkSentence("Sentence 3", "He doesn’t want to finish the class now.", 12170, 13060), HomeworkSentence("Sentence 4", "Where does she go at night?", 14360, 16980), HomeworkSentence("Sentence 5", "Does she like this?", 18560, 19450), HomeworkSentence("Sentence 6", "Cool, I have thirteen days to finish.", 20670, 22560), HomeworkSentence("Sentence 7", "Does it like the food?", 23930, 27390), HomeworkSentence("Sentence 8", "We have to study tomorrow morning.", 28880, 33740), HomeworkSentence("Sentence 9", "I have to eat before my class today.", 35020, 38470), HomeworkSentence("Sentence 10", "She wants to eat eighteen apples.", 40140, 44280), HomeworkSentence("Sentence 11", "They don’t have to finish twelve books.", 45500, 50000), HomeworkSentence("Sentence 12", "How are you today? I’m cool, thanks.", 51200, 56000), HomeworkSentence("Sentence 13", "What number does he want?", 57200, 62000), HomeworkSentence("Sentence 14", "We like to open the windows in the morning.", 63200, 68500), HomeworkSentence("Sentence 15", "Where does he study science?", 69700, 74800), HomeworkSentence("Sentence 16", "He has to work two months.", 76000, 81000), HomeworkSentence("Sentence 17", "Do you have to work after your class today?", 82200, 88000), HomeworkSentence("Sentence 18", "Sometimes I speak Spanish, but I always speak English.", 89200, 95800), HomeworkSentence("Sentence 19", "What things do you like to eat at home?", 97000, 102500), HomeworkSentence("Sentence 20", "My window doesn’t open today.", 103700, 109000), HomeworkSentence("Sentence 21", "Sorry, I don’t speak German.", 110200, 115000), HomeworkSentence("Sentence 22", "He eats hot things.", 116200, 121000), HomeworkSentence("Sentence 23", "What door do you wanna open now?", 122200, 127000), HomeworkSentence("Sentence 24", "What year do you have to go?", 128200, 133000), HomeworkSentence("Sentence 25", "She wants more apples and less bread.", 134200, 139500), HomeworkSentence("Sentence 26", "Excuse me, I have to come.", 140700, 146000), HomeworkSentence("Sentence 27", "Don’t worry, drink cold water and go.", 147200, 153000), HomeworkSentence("Sentence 28", "That’s it, you have to open thirteen doors.", 154200, 160000), HomeworkSentence("Sentence 29", "What days do you work this month?", 161200, 166000), HomeworkSentence("Sentence 30", "Where does she go at night?", 167200, 172000), HomeworkSentence("Sentence 31", "Please open your books and start the homework now.", 173200, 178800), HomeworkSentence("Sentence 32", "He starts things, but he doesn’t finish.", 180000, 186000), HomeworkSentence("Sentence 33", "Excuse me, do you speak French?", 187200, 193000), HomeworkSentence("Sentence 34", "I have to study in the morning, work in the afternoon and read at night.", 194200, 200800), HomeworkSentence("Sentence 35", "What month do we have to study?", 199470, 212160), HomeworkSentence("Sentence 36", "You help me today and I help you tomorrow, ok?", 214290, 219680), HomeworkSentence("Sentence 37", "I study before my class, but I eat before I study.", 221160, 226920), HomeworkSentence("Sentence 38", "He studies where you work.", 228500, 232640), HomeworkSentence("Sentence 39", "Help me, help you.", 234210, 237850)
    )
}

object Lesson6ListeningData {
    val AUDIO_RES_ID = R.raw.lesson6
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "We need rain this month.", 470, 14500), HomeworkSentence("Sentence 2", "How much water do you drink at night?", 15780, 20370), HomeworkSentence("Sentence 3", "It spells R-A-I-N.", 21680, 30780), HomeworkSentence("Sentence 4", "I eat lots of things in the mall.", 32790, 37910), HomeworkSentence("Sentence 5", "He prefers eggs than ham.", 39190, 40130), HomeworkSentence("Sentence 6", "Does she always drink a lot of coffee in the morning?", 41500, 44710), HomeworkSentence("Sentence 7", "Excuse me, do you learn French in this school?", 46130, 50920), HomeworkSentence("Sentence 8", "Where do you need to go with your friend now?", 52480, 58420), HomeworkSentence("Sentence 9", "My friend and I need to go to church to understand this.", 60000, 73700), HomeworkSentence("Sentence 10", "My mom always goes downtown to work.", 75510, 79480), HomeworkSentence("Sentence 11", "Do they need to learn science?", 80800, 86500), HomeworkSentence("Sentence 12", "They don’t need but they want to.", 87700, 93000), HomeworkSentence("Sentence 13", "What plate does he want today?", 94200, 99500), HomeworkSentence("Sentence 14", "I always drink a cup of coffee in the morning.", 100700, 106000), HomeworkSentence("Sentence 15", "For me coffee works ok.", 107200, 112000), HomeworkSentence("Sentence 16", "Rice, beans, eggs and meat, that’s it.", 113200, 118500), HomeworkSentence("Sentence 17", "Never mind, I prefer to learn English.", 119700, 125000), HomeworkSentence("Sentence 18", "What does he prefer, wine or beer?", 126200, 131000), HomeworkSentence("Sentence 19", "She likes wine very much.", 131920, 135620), HomeworkSentence("Sentence 20", "Does he prefer to study here or in your house?", 137460, 138510), HomeworkSentence("Sentence 21", "What? She prefers beer than water?", 139760, 143960), HomeworkSentence("Sentence 22", "Hey, what do you have for me?", 145290, 147780), HomeworkSentence("Sentence 23", "We always understand you.", 149550, 154100), HomeworkSentence("Sentence 24", "We don’t need this now, we need that.", 154100, 155200), HomeworkSentence("Sentence 25", "Where does she go after she comes from school?", 155200, 156756)
    )
}

object Lesson4SpokenData { val sentences = lesson4SpokenSentences }
object Lesson4WrittenData { val sentences = lesson4WrittenSentences }
object Lesson5SpokenData { val sentences = lesson5SpokenSentences }
object Lesson5WrittenData { val sentences = lesson5WrittenSentences }
object Lesson6SpokenData { val sentences = lesson6SpokenSentences }
object Lesson6WrittenData { val sentences = lesson6WrittenSentences }

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
    SpokenHomeworkSentence("Isso come a comida.", "It eats the food."),
    SpokenHomeworkSentence("Hoje é um bom dia.", "Today is a good day."),
    SpokenHomeworkSentence("Amanhã ele começa o trabalho.", "Tomorrow he starts the work."),
    SpokenHomeworkSentence("Ontem ela terminou a aula.", "Yesterday she finished the class."),
    SpokenHomeworkSentence("Por favor, abra a porta.", "Please open the door."),
    SpokenHomeworkSentence("Por favor, feche a janela.", "Please close the window."),
    SpokenHomeworkSentence("Desculpe, eu não entendo.", "Sorry, I don’t understand."),
    SpokenHomeworkSentence("Com licença, onde é a aula?", "Excuse me, where is the class?"),
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
    WrittenHomeworkSentence("Isso não abre.", "It doesn’t open."),
    WrittenHomeworkSentence("Isso começa agora.", "It starts now."),
    WrittenHomeworkSentence("Por favor, comece agora.", "Please start now."),
    WrittenHomeworkSentence("Por favor, termine hoje.", "Please finish today."),
    WrittenHomeworkSentence("Desculpe, ela não fala inglês.", "Sorry, she doesn’t speak English."),
    WrittenHomeworkSentence("Com licença, ele trabalha aqui?", "Excuse me, does he work here?"),
    WrittenHomeworkSentence("Não se preocupe, ela fecha a loja.", "Don’t worry, she closes the store."),
    WrittenHomeworkSentence("Legal, ele abre a janela.", "Cool, he opens the window."),
    WrittenHomeworkSentence("Às vezes ela estuda antes do trabalho.", "Sometimes she studies before work."),
    WrittenHomeworkSentence("Ela termina mais ou menos hoje.", "She finishes more or less today.")
)

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
