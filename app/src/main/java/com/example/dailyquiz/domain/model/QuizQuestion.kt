package com.example.dailyquiz.domain.model

data class QuizQuestion(
    val id: Int,
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
)