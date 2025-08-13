package com.example.dailyquiz.presentation.screen.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailyquiz.ui.components.AppButton
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.components.StarsRow
import com.example.dailyquiz.ui.theme.*

@Composable
fun ResultScreen(
    correct: Int,
    navController: NavController,
    categoryId: Int?,
    difficultyApi: String?
) {

    val categoryName = categoryId?.let { resolveCategoryTitle(it) }
    val difficultyName = difficultyApi?.let { resolveDifficultyTitle(it) }

    val title = resultTitle(correct)
    val subtitle = resultSubtitle(correct)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Результаты",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (categoryName != null || difficultyName != null) {
                Spacer(Modifier.height(12.dp))
                categoryName?.let {
                    Text(
                        text = "Категория: $it",
                        style = MaterialTheme.typography.titleMedium,
                        color = White,
                        textAlign = TextAlign.Center
                    )
                }
                difficultyName?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Сложность: $it",
                        style = MaterialTheme.typography.titleMedium,
                        color = White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            DailyCard(
                modifier = Modifier.fillMaxWidth(),
                corner = 46.dp,
                contentPaddingH = 24.dp,
                contentPaddingV = 32.dp
            ) {
                StarsRow(total = 5, correct = correct, size = 40.dp)
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "$correct из 5",
                    style = MaterialTheme.typography.titleMedium,
                    color = Yellow,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurfaceLight,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(52.dp))
                AppButton(
                    text = "НАЧАТЬ ЗАНОВО",
                    onClick = { navController.popBackStack(route = "quiz", inclusive = false) }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

private fun resultTitle(correct: Int): String = when (correct) {
    5 -> "Идеально!"
    4 -> "Почти идеально!"
    3 -> "Хороший результат!"
    2 -> "Есть над чем поработать"
    1 -> "Сложный вопрос?"
    else -> "Бывает и так!"
}

private fun resultSubtitle(correct: Int): String = when (correct) {
    5 -> "5/5 — вы ответили на всё правильно. Это блестящий результат!"
    4 -> "4/5 — очень близко к совершенству. Ещё один шаг!"
    3 -> "3/5 — вы на верном пути. Продолжайте тренироваться!"
    2 -> "2/5 — не расстраивайтесь, попробуйте ещё раз!"
    1 -> "1/5 — иногда просто не ваш день. Следующая попытка будет лучше!"
    else -> "0/5 — не отчаивайтесь. Начните заново и удивите себя!"
}

private fun resolveDifficultyTitle(d: String): String = when (d.lowercase()) {
    "easy" -> "Низкая"
    "medium" -> "Средняя"
    "hard" -> "Высокая"
    else -> d
}

private val categoryMap: Map<Int, String> = mapOf(
    9 to "Общие знания",
    17 to "Наука и природа",
    18 to "Компьютеры",
    22 to "География",
    23 to "История"
)

private fun resolveCategoryTitle(id: Int): String =
    categoryMap[id] ?: "Категория #$id"