package com.example.myenglish.data

import android.content.Context
import com.example.myenglish.R

object HomeworkData {
    val LESSON_1_AUDIO_RES_ID = R.raw.lesson1

    val lesson1Sentences = arrayOf(
        HomeworkSentence("Sentence 1", "I like.", 870, 2527),
        HomeworkSentence("Sentence 2", "You eat meat.", 3750, 5880),
        HomeworkSentence("Sentence 3", "I don't like.", 7610, 9426),
        HomeworkSentence("Sentence 4", "They don't drink water.", 11140, 13257),
        HomeworkSentence("Sentence 5", "Do you drink milk?", 15400, 17676),
        HomeworkSentence("Sentence 6", "Do we eat meat?", 19230, 21388),
        HomeworkSentence("Sentence 7", "I like bread with butter.", 23320, 26212),
        HomeworkSentence("Sentence 8", "I eat.", 28140, 29943),
        HomeworkSentence("Sentence 9", "You like.", 31790, 33424),
        HomeworkSentence("Sentence 10", "I don't like.", 35040, 37255),
        HomeworkSentence("Sentence 11", "You don't like.", 38820, 40794),
        HomeworkSentence("Sentence 12", "Do you like?", 42610, 44612),
        HomeworkSentence("Sentence 13", "Do they like?", 46060, 48280),
        HomeworkSentence("Sentence 14", "Don't you like?", 50060, 53014),
        HomeworkSentence("Sentence 15", "Don't they like?", 54040, 56920),
        HomeworkSentence("Sentence 16", "I like to eat.", 58100, 60369),
        HomeworkSentence("Sentence 17", "I eat in the morning.", 61660, 64021),
        HomeworkSentence("Sentence 18", "I drink milk in the morning.", 65550, 68597),
        HomeworkSentence("Sentence 19", "Do you like milk?", 70140, 72258),
        HomeworkSentence("Sentence 20", "They don't eat bread with butter.", 74010, 77491),
        HomeworkSentence("Sentence 21", "I don't like to eat in the morning.", 79160, 82187),
        HomeworkSentence("Sentence 22", "I drink water.", 83690, 85800),
        HomeworkSentence("Sentence 23", "I eat bread.", 87270, 89614),
        HomeworkSentence("Sentence 24", "I drink juice.", 91220, 93297),
        HomeworkSentence("Sentence 25", "I don't like milk.", 95200, 97238),
        HomeworkSentence("Sentence 26", "I don't drink in the morning.", 99010, 101591),
        HomeworkSentence("Sentence 27", "You eat.", 103310, 104970),
        HomeworkSentence("Sentence 28", "We like.", 106630, 108521),
        HomeworkSentence("Sentence 29", "They like to drink at night.", 110110, 112936),
        HomeworkSentence("Sentence 30", "We don't drink in the morning.", 114640, 116940),
        HomeworkSentence("Sentence 31", "Do you like butter?", 118630, 120577),
        HomeworkSentence("Sentence 32", "Do they like to drink beer?", 122320, 124916)
    )

    val lesson2Sentences = arrayOf(
        HomeworkSentence("Sentence 1", "You speak French.", 1060, 3990),
        HomeworkSentence("Sentence 2", "I don't want.", 5430, 8260),
        HomeworkSentence("Sentence 3", "We don’t want to speak french.", 10020, 14240),
        HomeworkSentence("Sentence 4", "Do they want milk?", 15690, 20000),
        HomeworkSentence("Sentence 5", "We wanna eat bread and ham", 20860, 26000),
        HomeworkSentence("Sentence 6", "I want to study there.", 26670, 31000),
        HomeworkSentence("Sentence 7", "I eat here in the morning.", 32000, 36140),
        HomeworkSentence("Sentence 8", "You study here.", 37420, 41500),
        HomeworkSentence("Sentence 9", "I study my small lesson.", 42390, 46470),
        HomeworkSentence("Sentence 10", "I don't want.", 47980, 51200),
        HomeworkSentence("Sentence 11", "We don’t wanna drink.", 51930, 55280),
        HomeworkSentence("Sentence 12", "Do you want water?", 56670, 60140),
        HomeworkSentence("Sentence 13", "Do they study german?", 61410, 65050),
        HomeworkSentence("Sentence 14", "Don’t you speak Portuguese?", 66340, 70450),
        HomeworkSentence("Sentence 15", "Don’t they want?", 71480, 75000),
        HomeworkSentence("Sentence 16", "I want to eat there.", 76180, 79960),
        HomeworkSentence("Sentence 17", "I study at night.", 81240, 85270),
        HomeworkSentence("Sentence 18", "We want milk in the afternoon.", 86310, 90790),
        HomeworkSentence("Sentence 19", "Do you want hot milk?", 91930, 95860),
        HomeworkSentence("Sentence 20", "Don’t they want to study English?", 96930, 101190),
        HomeworkSentence("Sentence 21", "I don’t want to eat in the morning.", 102690, 107120),
        HomeworkSentence("Sentence 22", "I don’t speak German.", 108530, 112220),
        HomeworkSentence("Sentence 23", "We study Spanish with you.", 113360, 117870),
        HomeworkSentence("Sentence 24", "Do you want to speak English?", 119090, 123600),
        HomeworkSentence("Sentence 25", "I don’t want to study math.", 124670, 128670),
        HomeworkSentence("Sentence 26", "They don’t speak with you.", 130120, 133980),
        HomeworkSentence("Sentence 27", "I study wine.", 135460, 139230),
        HomeworkSentence("Sentence 28", "Do you want music here?", 140980, 144810),
        HomeworkSentence("Sentence 29", "They want to drink at night.", 146460, 150650),
        HomeworkSentence("Sentence 30", "We don’t speak English in the morning.", 152010, 156480),
        HomeworkSentence("Sentence 31", "I don’t speak Portuguese with you.", 157900, 163500),
        HomeworkSentence("Sentence 32", "Do you wanna drink cold beer?", 164860, 169500)
    )

    fun sentencesForLesson(lessonName: String): Array<HomeworkSentence> {
        return if (lessonName == "Lesson 3") {
            Lesson3Homework1Audio.sentences
        } else if (lessonName == "Lesson 2") {
            lesson2Sentences
        } else {
            lesson1Sentences
        }
    }

    fun audioResIdForLesson(context: Context, lessonName: String): Int {
        if (lessonName == "Lesson 1") return LESSON_1_AUDIO_RES_ID
        if (lessonName == "Lesson 2") return context.resources.getIdentifier("lesson2", "raw", context.packageName)
        if (lessonName == "Lesson 3") return Lesson3Homework1Audio.AUDIO_RES_ID
        return 0
    }

    fun hasListeningHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" || lessonName == "Lesson 2" || lessonName == "Lesson 3"
    }
}