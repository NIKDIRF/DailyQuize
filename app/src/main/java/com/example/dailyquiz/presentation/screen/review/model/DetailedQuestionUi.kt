package com.example.dailyquiz.presentation.screen.review.model

data class DetailedQuestionUi(
    val index: Int,
    val total: Int = 5,
    val title: String,
    val answers: List<String>,
    val correctIndex: Int,
    val selectedIndex: Int?
)