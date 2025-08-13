package com.example.dailyquiz.di

import com.example.dailyquiz.data.session.QuizSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides
    @Singleton
    fun provideQuizSessionManager(): QuizSessionManager = QuizSessionManager()
}