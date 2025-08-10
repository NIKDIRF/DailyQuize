package com.example.dailyquiz.presentation.screen.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dailyquiz.R
import com.example.dailyquiz.ui.theme.*

data class DetailedQuestionUi(
    val index: Int,
    val total: Int = 5,
    val title: String,
    val answers: List<String>,
    val correctIndex: Int,
    val selectedIndex: Int?,
)

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
                    Button(
                        onClick = onStartOver,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(width = 260.dp, height = 50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = DarkPurple
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            text = "НАЧАТЬ ЗАНОВО",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkPurple
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    correct: Int,
    total: Int,
    onStartOver: () -> Unit
) {
    val (title, subtitle) = remember(correct, total) { resultCopy(correct, total) }

    Surface(
        color = SurfaceLight,
        shape = RoundedCornerShape(46.dp),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StarsRowSummary(correct = correct, total = total, size = 40.dp)
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
            Button(
                onClick = onStartOver,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "НАЧАТЬ ЗАНОВО",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StarsRowSummary(correct: Int, total: Int, size: Dp = 40.dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
    ) {
        repeat(total) { i ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier.size(size),
                tint = if (i < correct) Yellow else Grey
            )
        }
    }
}

@Composable
private fun QuestionResultCard(q: DetailedQuestionUi) {
    val isCorrectOverall = q.selectedIndex != null && q.selectedIndex == q.correctIndex

    Surface(
        color = SurfaceLight,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Вопрос ${q.index + 1} из ${q.total}",
                    style = MaterialTheme.typography.titleMedium,
                    color = LightPurple,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(
                        if (isCorrectOverall) R.drawable.ic_correct else R.drawable.ic_wrong
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
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
                val selected = q.selectedIndex == i
                val correct = q.correctIndex == i

                val outlineColor = when {
                    selected && correct -> Green
                    selected && !correct -> Red
                    else -> Color.Transparent
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(color = Cream, shape = RoundedCornerShape(16.dp))
                        .border(
                            width = if (outlineColor == Color.Transparent) 0.dp else 2.dp,
                            color = outlineColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        selected && correct -> {
                            Icon(
                                painter = painterResource(R.drawable.ic_correct),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                        selected && !correct -> {
                            Icon(
                                painter = painterResource(R.drawable.ic_wrong),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                        else -> {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .border(width = 2.dp, color = Grey, shape = CircleShape)
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = answer,
                        style = MaterialTheme.typography.titleMedium,
                        color = when {
                            selected && correct -> Green
                            selected && !correct -> Red
                            else -> OnSurfaceLight
                        }
                    )
                }

                if (i != q.answers.lastIndex) Spacer(Modifier.height(12.dp))
            }
        }
    }
}

private fun resultCopy(correct: Int, total: Int): Pair<String, String> {
    return when (correct) {
        5 -> "Идеально!" to "5/5 — вы ответили на всё правильно. Это блестящий результат!"
        4 -> "Почти идеально!" to "4/5 — очень близко к совершенству. Ещё один шаг!"
        3 -> "Хороший результат!" to "3/5 — вы на верном пути. Продолжайте тренироваться!"
        2 -> "Есть над чем поработать" to "2/5 — не расстраивайтесь, попробуйте ещё раз!"
        1 -> "Сложный вопрос?" to "1/5 — иногда просто не ваш день. Следующая попытка будет лучше!"
        else -> "Бывает и так!" to "0/5 — не отчаивайтесь. Начните заново и удивите себя!"
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Details / preview")
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