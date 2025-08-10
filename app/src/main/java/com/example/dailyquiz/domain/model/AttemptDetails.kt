package com.example.dailyquiz.domain.model

data class AttemptDetails(
    val attempt: Attempt,
    val answers: List<AttemptAnswer>
)
