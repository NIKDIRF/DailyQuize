package com.example.dailyquiz.presentation.screen.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dailyquiz.R
import com.example.dailyquiz.ui.theme.*

@Composable
fun ResultScreen(
    correct: Int,
    navController: NavController,
    viewModel: ResultViewModel = hiltViewModel()
) {
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
                    text = "Результат",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(40.dp))

            Surface(
                color = SurfaceLight,
                shape = RoundedCornerShape(46.dp),
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StarsRowResult(correct = correct, total = 5)

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "$correct из 5",
                        style = MaterialTheme.typography.titleMedium,
                        color = Yellow,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnSurfaceLight,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceLight,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { navController.popBackStack(route = "quiz", inclusive = false) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
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
    }
}

@Composable
private fun StarsRowResult(correct: Int, total: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        repeat(total) { i ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (i < correct) Yellow else Grey,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(Modifier.weight(1f))
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

@Preview(showBackground = true, showSystemUi = true, name = "Result 4/5")
@Composable
private fun PreviewResult() {
    DailyQuizTheme {
        ResultScreen(correct = 4, navController = rememberNavController())
    }
}