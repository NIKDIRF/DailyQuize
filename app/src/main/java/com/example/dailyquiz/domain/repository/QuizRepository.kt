package com.example.dailyquiz.domain.repository

import com.example.dailyquiz.domain.model.QuizQuestion

interface QuizRepository {
    suspend fun startQuiz(
        amount: Int = 5,
        type: String = "multiple",
        category: Int? = null,
        difficulty: String? = null
    ): List<QuizQuestion>
}