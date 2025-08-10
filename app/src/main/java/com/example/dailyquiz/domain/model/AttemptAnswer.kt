package com.example.dailyquiz.domain.model

data class AttemptAnswer(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctIndex: Int,
    val selectedIndex: Int?
)