package com.example.dailyquiz.presentation.navigation

object Routes {

    const val QUIZ_GRAPH = "quiz_graph"

    const val QUIZ = "quiz"

    private const val QUESTION_ARG = "index"
    const val QUESTION = "question/{$QUESTION_ARG}"
    fun question(index: Int) = "question/$index"

    private const val RESULT_CORRECT = "correct"
    private const val RESULT_CATEGORY = "category"
    private const val RESULT_DIFFICULTY = "difficulty"

    const val RESULT = "result/{$RESULT_CORRECT}/{$RESULT_CATEGORY}/{$RESULT_DIFFICULTY}"

    fun result(correct: Int, categoryId: Int?, difficulty: String?) =
        "result/$correct/${categoryId ?: -1}/${difficulty ?: "none"}"

    const val HISTORY = "history"

    private const val ATTEMPT_ARG = "attemptId"
    const val ATTEMPT = "attempt/{$ATTEMPT_ARG}"
    fun attempt(id: Long) = "attempt/$id"
}