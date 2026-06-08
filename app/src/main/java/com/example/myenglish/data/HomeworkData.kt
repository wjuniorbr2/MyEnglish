package com.example.myenglish.data

import android.content.Context

object HomeworkData {

    fun sentencesForLesson(lessonName: String): Array<HomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> Lesson1ListeningData.sentences
            "Lesson 2" -> Lesson2ListeningData.sentences
            "Lesson 3" -> Lesson3ListeningData.sentences
            "Lesson 4" -> Lesson4ListeningData.sentences
            "Lesson 5" -> Lesson5ListeningData.sentences
            "Lesson 6" -> Lesson6ListeningData.sentences
            else -> emptyArray()
        }
    }

    fun audioResIdForLesson(context: Context, lessonName: String): Int {
        return when (lessonName) {
            "Lesson 1" -> Lesson1ListeningData.AUDIO_RES_ID
            "Lesson 2" -> Lesson2ListeningData.AUDIO_RES_ID
            "Lesson 3" -> Lesson3ListeningData.AUDIO_RES_ID
            "Lesson 4" -> Lesson4ListeningData.AUDIO_RES_ID
            "Lesson 5" -> Lesson5ListeningData.AUDIO_RES_ID
            "Lesson 6" -> Lesson6ListeningData.AUDIO_RES_ID
            else -> 0
        }
    }

    fun spokenSentencesForLesson(lessonName: String): Array<SpokenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> Lesson1SpokenData.sentences
            "Lesson 2" -> Lesson2SpokenData.sentences
            "Lesson 3" -> Lesson3SpokenData.sentences
            "Lesson 4" -> Lesson4SpokenData.sentences
            "Lesson 5" -> Lesson5SpokenData.sentences
            "Lesson 6" -> Lesson6SpokenData.sentences
            else -> emptyArray()
        }
    }

    fun writtenSentencesForLesson(lessonName: String): Array<WrittenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> Lesson1WrittenData.sentences
            "Lesson 2" -> Lesson2WrittenData.sentences
            "Lesson 3" -> Lesson3WrittenData.sentences
            "Lesson 4" -> Lesson4WrittenData.sentences
            "Lesson 5" -> Lesson5WrittenData.sentences
            "Lesson 6" -> Lesson6WrittenData.sentences
            else -> emptyArray()
        }
    }

    fun hasListeningHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3" ||
                lessonName == "Lesson 4" ||
                lessonName == "Lesson 5" ||
                lessonName == "Lesson 6"
    }

    fun hasSpokenHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3" ||
                lessonName == "Lesson 4" ||
                lessonName == "Lesson 5" ||
                lessonName == "Lesson 6"
    }

    fun hasWrittenHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3" ||
                lessonName == "Lesson 4" ||
                lessonName == "Lesson 5" ||
                lessonName == "Lesson 6"
    }
}
