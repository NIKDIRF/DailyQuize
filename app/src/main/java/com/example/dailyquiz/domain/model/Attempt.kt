package com.example.dailyquiz.domain.model

data class Attempt(
    val id: Long = 0L,
    val timestamp: Long,
    val correct: Int,
    val total: Int,
    val category: Int? = null,
    val difficulty: String? = null
)