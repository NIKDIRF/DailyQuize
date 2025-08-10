package com.example.dailyquiz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val timestamp: Long,
    val correct: Int,
    val total: Int,
    val category: Int? = null,
    val difficulty: String? = null
)