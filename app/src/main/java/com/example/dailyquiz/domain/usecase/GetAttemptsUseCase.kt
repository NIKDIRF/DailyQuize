package com.example.dailyquiz.domain.usecase

import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAttemptsUseCase @Inject constructor(
    private val repo: HistoryRepository
) {
    operator fun invoke(): Flow<List<Attempt>> = repo.observeAttempts()
}