package com.example.dailyquiz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = White,
    background = Purple,
    onBackground = White,
    surface = White,
    onSurface = Black
)

@Composable
fun DailyQuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}