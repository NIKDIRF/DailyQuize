package com.example.dailyquiz.data.remote.dto

import com.squareup.moshi.Json

data class TriviaResponseDto(
    @Json(name = "response_code") val responseCode: Int,
    val results: List<QuestionDto>
)

data class QuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @Json(name = "correct_answer") val correctAnswer: String,
    @Json(name = "incorrect_answers") val incorrectAnswers: List<String>
)