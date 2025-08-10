package com.example.dailyquiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dailyquiz.data.local.entity.AttemptEntity
import com.example.dailyquiz.data.local.entity.AttemptAnswerEntity

@Database(
    entities = [
        AttemptEntity::class,
        AttemptAnswerEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun attemptDao(): AttemptDao
}