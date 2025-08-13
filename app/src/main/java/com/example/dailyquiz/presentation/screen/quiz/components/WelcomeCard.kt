package com.example.dailyquiz.presentation.screen.quiz.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.components.PrimaryButton

@Composable
fun WelcomeCard(
    onPrimaryClick: () -> Unit,
    showError: Boolean
) {
    DailyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Добро пожаловать\nв DailyQuiz!",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(40.dp))
        PrimaryButton(
            text = "НАЧАТЬ ВИКТОРИНУ",
            onClick = onPrimaryClick,
            modifier = Modifier.fillMaxWidth()
        )
        if (showError) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Ошибка! Попробуйте ещё раз",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}