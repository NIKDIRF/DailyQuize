package com.example.dailyquiz.data.remote

import com.example.dailyquiz.data.remote.dto.TriviaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 5,
        @Query("type") type: String = "multiple",
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null
    ): TriviaResponseDto
}