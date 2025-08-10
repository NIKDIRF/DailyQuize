package com.example.dailyquiz.di

import com.example.dailyquiz.data.repository.HistoryRepositoryImpl
import com.example.dailyquiz.data.repository.QuizRepositoryImpl
import com.example.dailyquiz.domain.repository.HistoryRepository
import com.example.dailyquiz.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuizRepository(impl: QuizRepositoryImpl): QuizRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
}