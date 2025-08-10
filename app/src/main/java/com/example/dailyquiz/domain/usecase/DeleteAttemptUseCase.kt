package com.example.dailyquiz.domain.usecase

import com.example.dailyquiz.domain.repository.HistoryRepository
import javax.inject.Inject

class DeleteAttemptUseCase @Inject constructor(
    private val repo: HistoryRepository
) {
    suspend operator fun invoke(id: Long) = repo.deleteAttempt(id)
}