package com.example.dailyquiz.presentation.navigation

object Routes {

    const val QUIZ = "quiz"

    private const val QUESTION_ARG = "index"
    const val QUESTION = "question/{$QUESTION_ARG}"
    fun question(index: Int) = "question/$index"

    private const val RESULT_ARG = "correct"
    const val RESULT = "result/{$RESULT_ARG}"
    fun result(correct: Int) = "result/$correct"

    const val HISTORY = "history"

    private const val ATTEMPT_ARG = "attemptId"
    const val ATTEMPT = "attempt/{$ATTEMPT_ARG}"
    fun attempt(id: Long) = "attempt/$id"
}