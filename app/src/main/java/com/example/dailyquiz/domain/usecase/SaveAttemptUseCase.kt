package com.example.dailyquiz.domain.usecase

import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.domain.model.AttemptAnswer
import com.example.dailyquiz.domain.repository.HistoryRepository
import javax.inject.Inject

class SaveAttemptUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(
        correct: Int,
        total: Int,
        timestamp: Long = System.currentTimeMillis(),
        category: Int? = null,
        difficulty: String? = null,
        answers: List<AttemptAnswer> = emptyList()
    ) {
        val attempt = Attempt(
            timestamp = timestamp,
            correct = correct.coerceIn(0, total),
            total = total,
            category = category,
            difficulty = difficulty
        )
        historyRepository.saveAttempt(attempt, answers)
    }
}