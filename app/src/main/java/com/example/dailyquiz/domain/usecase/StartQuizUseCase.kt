package com.example.dailyquiz.domain.usecase

import com.example.dailyquiz.domain.model.QuizQuestion
import com.example.dailyquiz.domain.repository.QuizRepository
import javax.inject.Inject

class StartQuizUseCase @Inject constructor(
    private val repo: QuizRepository
) {
    suspend operator fun invoke(
        category: Int? = null,
        difficulty: String? = null
    ): List<QuizQuestion> = repo.startQuiz(
        amount = 5,
        type = "multiple",
        category = category,
        difficulty = difficulty
    )
}