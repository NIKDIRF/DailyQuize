package com.example.dailyquiz.data.repository

import android.util.Log
import com.example.dailyquiz.data.mapper.toDomain
import com.example.dailyquiz.data.remote.TriviaApi
import com.example.dailyquiz.domain.model.QuizQuestion
import com.example.dailyquiz.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val api: TriviaApi
) : QuizRepository {

    override suspend fun startQuiz(
        amount: Int,
        type: String,
        category: Int?,
        difficulty: String?
    ): List<QuizQuestion> {
        val tag = "QuizRepository"
        Log.d(tag, "Request: amount=$amount, type=$type, category=$category, difficulty=$difficulty")

        val resp = api.getQuestions(
            amount = amount, type = type, category = category, difficulty = difficulty
        )

        Log.d(tag, "Response: code=${resp.responseCode}, results=${resp.results.size}")

        if (resp.responseCode != 0) {
            throw IllegalStateException("API error: response_code=${resp.responseCode}")
        }

        return resp.results.mapIndexed { idx, dto -> dto.toDomain(id = idx) }.also { list ->
            list.forEach { q ->
                Log.d(tag, "Q${q.id + 1}: ${q.text.take(60)}... correctIndex=${q.correctIndex}")
            }
        }
    }
}