package com.example.dailyquiz.presentation.screen.review.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.components.PrimaryButton
import com.example.dailyquiz.ui.components.StarsRow
import com.example.dailyquiz.ui.theme.OnSurfaceLight
import com.example.dailyquiz.ui.theme.Yellow

@Composable
fun SummaryCard(
    correct: Int,
    total: Int,
    onStartOver: () -> Unit
) {
    val (title, subtitle) = remember(correct, total) { resultCopy(correct, total) }

    DailyCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StarsRow(total = total, correct = correct, size = 40.dp)
            Spacer(Modifier.height(24.dp))
            Text(
                text = "$correct из $total",
                style = MaterialTheme.typography.titleMedium,
                color = Yellow,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = OnSurfaceLight,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(23.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceLight,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(52.dp))
            PrimaryButton(text = "НАЧАТЬ ЗАНОВО", onClick = onStartOver)
        }
    }
}

private fun resultCopy(correct: Int, total: Int): Pair<String, String> = when (correct) {
    5 -> "Идеально!" to "5/5 — вы ответили на всё правильно. Это блестящий результат!"
    4 -> "Почти идеально!" to "4/5 — очень близко к совершенству. Ещё один шаг!"
    3 -> "Хороший результат!" to "3/5 — вы на верном пути. Продолжайте тренироваться!"
    2 -> "Есть над чем поработать" to "2/5 — не расстраивайтесь, попробуйте ещё раз!"
    1 -> "Сложный вопрос?" to "1/5 — иногда просто не ваш день. Следующая попытка будет лучше!"
    else -> "Бывает и так!" to "0/5 — не отчаивайтесь. Начните заново и удивите себя!"
}