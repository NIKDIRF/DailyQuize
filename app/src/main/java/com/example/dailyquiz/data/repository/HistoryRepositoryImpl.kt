package com.example.dailyquiz.data.repository

import com.example.dailyquiz.data.local.AttemptDao
import com.example.dailyquiz.data.local.entity.AttemptAnswerEntity
import com.example.dailyquiz.data.local.entity.AttemptEntity
import com.example.dailyquiz.data.local.entity.AttemptWithAnswers
import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.domain.model.AttemptAnswer
import com.example.dailyquiz.domain.model.AttemptDetails
import com.example.dailyquiz.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val dao: AttemptDao
) : HistoryRepository {

    override fun observeAttempts(): Flow<List<Attempt>> =
        dao.observeAttempts().map { list -> list.map { it.toDomain() } }

    override fun observeAttemptDetails(id: Long): Flow<AttemptDetails?> =
        dao.observeAttemptWithAnswers(id).map { it?.toDomain() }

    override suspend fun saveAttempt(attempt: Attempt, answers: List<AttemptAnswer>) {
        val id = dao.insertAttempt(attempt.toEntity())
        if (answers.isNotEmpty()) {
            val rows = answers.map {
                AttemptAnswerEntity(
                    attemptId = id,
                    indexInAttempt = it.index,
                    question = it.question,
                    answer0 = it.answers.getOrNull(0).orEmpty(),
                    answer1 = it.answers.getOrNull(1).orEmpty(),
                    answer2 = it.answers.getOrNull(2).orEmpty(),
                    answer3 = it.answers.getOrNull(3).orEmpty(),
                    correctIndex = it.correctIndex,
                    selectedIndex = it.selectedIndex
                )
            }
            dao.insertAnswers(rows)
        }
    }

    override suspend fun deleteAttempt(id: Long) {
        dao.deleteAttempt(id)
    }

    private fun AttemptEntity.toDomain() = Attempt(
        id = id,
        timestamp = timestamp,
        correct = correct,
        total = total,
        category = category,
        difficulty = difficulty
    )

    private fun Attempt.toEntity() = AttemptEntity(
        id = if (id == 0L) 0L else id,
        timestamp = timestamp,
        correct = correct,
        total = total,
        category = category,
        difficulty = difficulty
    )

    private fun AttemptWithAnswers.toDomain(): AttemptDetails {
        val attemptDomain = attempt.toDomain()
        val answersDomain = answers
            .sortedBy { it.indexInAttempt }
            .map { it.toDomain() }
        return AttemptDetails(attempt = attemptDomain, answers = answersDomain)
    }

    private fun AttemptAnswerEntity.toDomain() = AttemptAnswer(
        index = indexInAttempt,
        question = question,
        answers = listOf(answer0, answer1, answer2, answer3),
        correctIndex = correctIndex,
        selectedIndex = selectedIndex
    )
}