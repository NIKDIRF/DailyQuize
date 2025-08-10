package com.example.dailyquiz.data.local

import androidx.room.*
import com.example.dailyquiz.data.local.entity.AttemptAnswerEntity
import com.example.dailyquiz.data.local.entity.AttemptEntity
import com.example.dailyquiz.data.local.entity.AttemptWithAnswers
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {

    @Query("SELECT * FROM attempts ORDER BY timestamp DESC")
    fun observeAttempts(): Flow<List<AttemptEntity>>

    @Transaction
    @Query("SELECT * FROM attempts WHERE id = :id")
    fun observeAttemptWithAnswers(id: Long): Flow<AttemptWithAnswers?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(entity: AttemptEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(items: List<AttemptAnswerEntity>)

    @Query("DELETE FROM attempts WHERE id = :id")
    suspend fun deleteAttempt(id: Long)
}