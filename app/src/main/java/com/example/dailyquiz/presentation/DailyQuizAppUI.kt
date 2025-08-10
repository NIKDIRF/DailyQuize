package com.example.dailyquiz.presentation

import androidx.compose.runtime.Composable
import com.example.dailyquiz.presentation.navigation.NavGraph
import com.example.dailyquiz.ui.theme.DailyQuizTheme

@Composable
fun DailyQuizAppUI() {
    DailyQuizTheme {
        NavGraph()
    }
}