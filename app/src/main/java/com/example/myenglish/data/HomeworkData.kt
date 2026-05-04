package com.example.myenglish.data

import android.content.Context

object HomeworkData {

    fun sentencesForLesson(lessonName: String): Array<HomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> Lesson1ListeningData.sentences
            "Lesson 2" -> Lesson2ListeningData.sentences
            "Lesson 3" -> Lesson3ListeningData.sentences
            else -> emptyArray()
        }
    }

    fun audioResIdForLesson(context: Context, lessonName: String): Int {
        return when (lessonName) {
            "Lesson 1" -> Lesson1ListeningData.AUDIO_RES_ID
            "Lesson 2" -> Lesson2ListeningData.AUDIO_RES_ID
            "Lesson 3" -> Lesson3ListeningData.AUDIO_RES_ID
            else -> 0
        }
    }

    fun spokenSentencesForLesson(lessonName: String): Array<SpokenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> Lesson1SpokenData.sentences
            "Lesson 2" -> Lesson2SpokenData.sentences
            "Lesson 3" -> Lesson3SpokenData.sentences
            else -> emptyArray()
        }
    }

    fun writtenSentencesForLesson(lessonName: String): Array<WrittenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> Lesson1WrittenData.sentences
            "Lesson 2" -> Lesson2WrittenData.sentences
            "Lesson 3" -> Lesson3WrittenData.sentences
            else -> emptyArray()
        }
    }

    fun hasListeningHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3"
    }

    fun hasSpokenHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3"
    }

    fun hasWrittenHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3"
    }
}