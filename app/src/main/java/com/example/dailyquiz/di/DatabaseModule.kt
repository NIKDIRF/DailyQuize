package com.example.dailyquiz.di

import android.content.Context
import androidx.room.Room
import com.example.dailyquiz.data.local.AppDatabase
import com.example.dailyquiz.data.local.AttemptDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "dailyquiz.db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    @Singleton
    fun provideAttemptDao(db: AppDatabase): AttemptDao = db.attemptDao()
}