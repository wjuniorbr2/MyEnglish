package com.example.myenglish.data

import android.content.Context

object HomeworkData {

    fun sentencesForLesson(lessonName: String): Array<HomeworkSentence> {
        val sentences = when (lessonName) {
            "Lesson 1" -> Lesson1ListeningData.sentences
            "Lesson 2" -> Lesson2ListeningData.sentences
            "Lesson 3" -> Lesson3ListeningData.sentences
            "Lesson 4" -> Lesson4ListeningData.sentences
            "Lesson 5" -> Lesson5ListeningData.sentences
            "Lesson 6" -> Lesson6ListeningData.sentences
            else -> emptyArray()
        }

        return if (lessonNumber(lessonName) < 11) {
            sentences
                .filterNot { containsEarlyVerbToBe(it.correctText) }
                .mapIndexed { index, sentence ->
                    sentence.copy(
                        label = if (lessonName == "Lesson 5") {
                            sentence.label
                        } else {
                            "Sentence ${index + 1}"
                        },
                        correctText = correctedListeningText(
                            lessonName = lessonName,
                            text = sentence.correctText
                        )
                    )
                }
                .toTypedArray()
        } else {
            sentences
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
        val sentences = when (lessonName) {
            "Lesson 1" -> Lesson1SpokenData.sentences
            "Lesson 2" -> Lesson2SpokenData.sentences
            "Lesson 3" -> Lesson3SpokenData.sentences
            "Lesson 4" -> Lesson4SpokenData.sentences
            "Lesson 5" -> Lesson5SpokenData.sentences
            "Lesson 6" -> Lesson6SpokenData.sentences
            else -> emptyArray()
        }

        return if (lessonNumber(lessonName) < 11) {
            sentences.filterNot { containsEarlyVerbToBe(it.english) }.toTypedArray()
        } else {
            sentences
        }
    }

    fun writtenSentencesForLesson(lessonName: String): Array<WrittenHomeworkSentence> {
        val sentences = when (lessonName) {
            "Lesson 1" -> Lesson1WrittenData.sentences
            "Lesson 2" -> Lesson2WrittenData.sentences
            "Lesson 3" -> Lesson3WrittenData.sentences
            "Lesson 4" -> Lesson4WrittenData.sentences
            "Lesson 5" -> Lesson5WrittenData.sentences
            "Lesson 6" -> Lesson6WrittenData.sentences
            else -> emptyArray()
        }

        return if (lessonNumber(lessonName) < 11) {
            sentences.filterNot { containsEarlyVerbToBe(it.english) }.toTypedArray()
        } else {
            sentences
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

    private fun correctedListeningText(lessonName: String, text: String): String {
        if (lessonName != "Lesson 5") return text

        return when (text) {
            "Excuse me, I have to come." -> "Excuse me, I have to go now."
            "What month do we have to study?" -> "What month do we have to start?"
            else -> text
        }
    }

    private fun lessonNumber(lessonName: String): Int {
        return lessonName.removePrefix("Lesson ").toIntOrNull() ?: Int.MAX_VALUE
    }
}
