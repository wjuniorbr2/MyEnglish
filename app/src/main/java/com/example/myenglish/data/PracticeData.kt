package com.example.myenglish.data

object PracticeData {
    fun sentencesForLesson(lessonName: String): Array<SpokenHomeworkSentence> {
        return when (lessonName) {
            "Lesson 1" -> mergePractice(Lesson1SpokenData.sentences, Lesson1WrittenData.sentences)
            "Lesson 2" -> mergePractice(Lesson2SpokenData.sentences, Lesson2WrittenData.sentences)
            "Lesson 3" -> mergePractice(Lesson3SpokenData.sentences, Lesson3WrittenData.sentences)
            else -> emptyArray()
        }
    }

    fun hasPractice(lessonName: String): Boolean {
        return lessonName == "Lesson 1" || lessonName == "Lesson 2" || lessonName == "Lesson 3"
    }

    private fun mergePractice(
        spoken: Array<SpokenHomeworkSentence>,
        written: Array<WrittenHomeworkSentence>
    ): Array<SpokenHomeworkSentence> {
        val list = mutableListOf<SpokenHomeworkSentence>()
        val seen = mutableSetOf<String>()

        fun add(portuguese: String, english: String) {
            val key = english.lowercase().trim()
            if (!seen.contains(key)) {
                seen.add(key)
                list.add(SpokenHomeworkSentence(portuguese, english))
            }
        }

        spoken.forEach { add(it.portuguese, it.english) }
        written.forEach { add(it.portuguese, it.english) }

        return list.toTypedArray()
    }
}
