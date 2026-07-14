package com.example.myenglish.data

object PracticeData {
    fun sentencesForLesson(lessonName: String): Array<SpokenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> mergePractice(Lesson1SpokenData.sentences, Lesson1WrittenData.sentences)
            "Lesson 2" -> mergePractice(Lesson2SpokenData.sentences, Lesson2WrittenData.sentences)
            "Lesson 3" -> mergePractice(Lesson3SpokenData.sentences, Lesson3WrittenData.sentences)
            "Lesson 4" -> mergePractice(Lesson4SpokenData.sentences, Lesson4WrittenData.sentences)
            "Lesson 5" -> mergePractice(Lesson5SpokenData.sentences, Lesson5WrittenData.sentences)
            "Lesson 6" -> mergePractice(Lesson6SpokenData.sentences, Lesson6WrittenData.sentences)
            else -> emptyArray()
        }
    }

    fun writtenSentencesForLesson(lessonName: String): Array<WrittenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> mergeWrittenPractice(
                Lesson1SpokenData.sentences to Lesson1WrittenData.sentences
            )
            "Lesson 2" -> mergeWrittenPractice(
                Lesson2SpokenData.sentences to Lesson2WrittenData.sentences
            )
            "Lesson 3" -> mergeWrittenPractice(
                Lesson3SpokenData.sentences to Lesson3WrittenData.sentences
            )
            "Lesson 4" -> mergeWrittenPractice(
                Lesson4SpokenData.sentences to Lesson4WrittenData.sentences
            )
            "Lesson 5" -> mergeWrittenPractice(
                Lesson5SpokenData.sentences to Lesson5WrittenData.sentences
            )
            "Lesson 6" -> mergeWrittenPractice(
                Lesson6SpokenData.sentences to Lesson6WrittenData.sentences
            )
            else -> emptyArray()
        }
    }

    fun listeningSentencesForLesson(lessonName: String): Array<WrittenHomeworkSentence> {
        return writtenSentencesForLesson(lessonName)
    }

    fun hasPractice(lessonName: String): Boolean {
        return lessonName == "Lesson 1" ||
                lessonName == "Lesson 2" ||
                lessonName == "Lesson 3" ||
                lessonName == "Lesson 4" ||
                lessonName == "Lesson 5" ||
                lessonName == "Lesson 6"
    }

    private fun mergePractice(
        spoken: Array<SpokenHomeworkSentence>,
        written: Array<WrittenHomeworkSentence>
    ): Array<SpokenHomeworkSentence> {
        val list = mutableListOf<SpokenHomeworkSentence>()
        val seen = mutableSetOf<String>()

        fun add(portuguese: String, english: String) {
            val key = english.lowercase().trim()
            if (!containsEarlyVerbToBe(english) && seen.add(key)) {
                list.add(SpokenHomeworkSentence(portuguese, english))
            }
        }

        spoken.forEach { add(it.portuguese, it.english) }
        written.forEach { add(it.portuguese, it.english) }

        return list.toTypedArray()
    }

    private fun mergeWrittenPractice(
        vararg sources: Pair<Array<SpokenHomeworkSentence>, Array<WrittenHomeworkSentence>>
    ): Array<WrittenHomeworkSentence> {
        val list = mutableListOf<WrittenHomeworkSentence>()
        val seen = mutableSetOf<String>()

        fun add(portuguese: String, english: String) {
            val key = english.lowercase().trim()
            if (!containsEarlyVerbToBe(english) && seen.add(key)) {
                list.add(WrittenHomeworkSentence(portuguese, english))
            }
        }

        sources.forEach { source ->
            source.first.forEach { add(it.portuguese, it.english) }
            source.second.forEach { add(it.portuguese, it.english) }
        }

        return list.toTypedArray()
    }
}
