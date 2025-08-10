package com.example.dailyquiz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "attempt_answers",
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("attemptId")]
)
data class AttemptAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val attemptId: Long,
    val indexInAttempt: Int,
    val question: String,
    val answer0: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val correctIndex: Int,
    val selectedIndex: Int? = null
)
