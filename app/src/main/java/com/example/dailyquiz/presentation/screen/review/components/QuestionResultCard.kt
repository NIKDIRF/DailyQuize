package com.example.dailyquiz.presentation.screen.review.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import com.example.dailyquiz.R
import com.example.dailyquiz.presentation.screen.review.model.DetailedQuestionUi
import com.example.dailyquiz.ui.components.AnswerOption
import com.example.dailyquiz.ui.components.AnswerState
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.theme.LightPurple
import com.example.dailyquiz.ui.theme.OnSurfaceLight

@Composable
fun QuestionResultCard(q: DetailedQuestionUi) {
    DailyCard(corner = 28.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Вопрос ${q.index + 1} из ${q.total}",
                style = MaterialTheme.typography.titleMedium,
                color = LightPurple,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            val isCorrect = q.selectedIndex != null && q.selectedIndex == q.correctIndex
            Icon(
                painter = painterResource(if (isCorrect) R.drawable.ic_correct else R.drawable.ic_wrong),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = q.title,
            style = MaterialTheme.typography.titleLarge,
            color = OnSurfaceLight,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        q.answers.forEachIndexed { i, answer ->
            val state = when {
                q.selectedIndex == i && q.correctIndex == i -> AnswerState.Correct
                q.selectedIndex == i && q.correctIndex != i -> AnswerState.Wrong
                else -> AnswerState.Unselected
            }
            AnswerOption(
                text = answer,
                state = state,
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
            if (i != q.answers.lastIndex) Spacer(Modifier.height(12.dp))
        }
    }
}