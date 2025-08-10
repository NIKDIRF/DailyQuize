package com.example.dailyquiz.domain.session

import com.example.dailyquiz.domain.model.AttemptAnswer
import com.example.dailyquiz.domain.model.QuizQuestion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizSession @Inject constructor() {

    var questions: List<QuizQuestion> = emptyList()
        private set

    fun set(questions: List<QuizQuestion>) { this.questions = questions }
    fun clear() { questions = emptyList() }

    fun buildAnswers(selections: List<Int?>): List<AttemptAnswer> {
        if (questions.isEmpty()) return emptyList()
        return questions.mapIndexed { idx, q ->
            AttemptAnswer(
                index = idx,
                question = q.text,
                answers = q.answers,
                correctIndex = q.correctIndex,
                selectedIndex = selections.getOrNull(idx)
            )
        }
    }
}