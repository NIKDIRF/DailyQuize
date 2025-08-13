package com.example.dailyquiz.presentation.screen.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dailyquiz.presentation.screen.review.components.QuestionResultCard
import com.example.dailyquiz.presentation.screen.review.components.SummaryCard
import com.example.dailyquiz.presentation.screen.review.model.DetailedQuestionUi
import com.example.dailyquiz.ui.components.SecondaryButton
import com.example.dailyquiz.ui.theme.DailyQuizTheme
import com.example.dailyquiz.ui.theme.Purple
import com.example.dailyquiz.ui.theme.White

@Composable
fun AttemptDetailsScreen(
    navController: NavController,
    correct: Int,
    total: Int,
    questions: List<DetailedQuestionUi>,
    onStartOver: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Purple)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Spacer(Modifier.height(38.dp))
                Text(
                    text = "Результаты",
                    style = MaterialTheme.typography.headlineMedium,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
                SummaryCard(correct = correct, total = total, onStartOver = onStartOver)
            }
            items(questions) { q ->
                QuestionResultCard(q)
            }

            item {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SecondaryButton(
                        text = "НАЧАТЬ ЗАНОВО",
                        onClick = onStartOver,
                        modifier = Modifier
                            .width(260.dp)
                            .height(50.dp),
                        elevation = 6.dp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Details / screen")
@Composable
private fun PreviewAttemptDetailsScreen() {
    DailyQuizTheme {
        val questions = listOf(
            DetailedQuestionUi(
                index = 0,
                title = "Как переводится слово «apple»?",
                answers = listOf("Груша", "Яблоко", "Апельсин", "Ананас"),
                correctIndex = 1,
                selectedIndex = 1
            ),
            DetailedQuestionUi(
                index = 1,
                title = "Какое слово означает цвет?",
                answers = listOf("Table", "Blue", "Run", "Chair"),
                correctIndex = 1,
                selectedIndex = 1
            ),
            DetailedQuestionUi(
                index = 2,
                title = "Как переводится фраза «good morning»",
                answers = listOf("Добрый вечер", "Привет", "Доброе утро", "Спокойной ночи"),
                correctIndex = 2,
                selectedIndex = 1
            )
        )
        AttemptDetailsScreen(
            navController = rememberNavController(),
            correct = 4,
            total = 5,
            questions = questions,
            onStartOver = {}
        )
    }
}