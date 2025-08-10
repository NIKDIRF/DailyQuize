package com.example.dailyquiz.domain.repository

import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.domain.model.AttemptAnswer
import com.example.dailyquiz.domain.model.AttemptDetails
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeAttempts(): Flow<List<Attempt>>

    fun observeAttemptDetails(id: Long): Flow<AttemptDetails?>

    suspend fun saveAttempt(
        attempt: Attempt,
        answers: List<AttemptAnswer> = emptyList()
    )

    suspend fun deleteAttempt(id: Long)
}