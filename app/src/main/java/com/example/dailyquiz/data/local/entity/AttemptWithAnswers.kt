package com.example.dailyquiz.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AttemptWithAnswers(
    @Embedded val attempt: AttemptEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "attemptId"
    )
    val answers: List<AttemptAnswerEntity>
)
