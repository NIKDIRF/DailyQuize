package com.example.dailyquiz.presentation.screen.question

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import com.example.dailyquiz.R
import com.example.dailyquiz.presentation.navigation.Routes
import com.example.dailyquiz.ui.theme.Black
import com.example.dailyquiz.ui.theme.Cream
import com.example.dailyquiz.ui.theme.DailyQuizTheme
import com.example.dailyquiz.ui.theme.DarkPurple
import com.example.dailyquiz.ui.theme.Grey
import com.example.dailyquiz.ui.theme.LightPurple
import com.example.dailyquiz.ui.theme.OnSurfaceLight
import com.example.dailyquiz.ui.theme.Purple
import com.example.dailyquiz.ui.theme.SurfaceLight
import com.example.dailyquiz.ui.theme.White

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun QuestionScreen(
    navController: NavController,
    index: Int
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry(Routes.QUIZ) }
    val viewModel: QuestionViewModel = hiltViewModel(parentEntry)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is QuestionViewModel.UiEffect.NavigateNext ->
                    navController.navigate(Routes.question(effect.nextIndex))
                is QuestionViewModel.UiEffect.NavigateToResult ->
                    navController.navigate(Routes.result(effect.correct)) {
                        launchSingleTop = true
                    }
            }
        }
    }

    LaunchedEffect(index) { viewModel.bindIndex(index) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLast = state.question.index == state.question.total - 1

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

            Image(
                painter = painterResource(R.drawable.logo_dailyquiz),
                contentDescription = "DAILYQUIZ",
                modifier = Modifier.size(width = 180.dp, height = 40.dp),
                contentScale = ContentScale.Fit
            )

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
                    Text(
                        text = "Вопрос ${state.question.index + 1} из ${state.question.total}",
                        style = MaterialTheme.typography.titleMedium,
                        color = LightPurple,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = state.question.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = OnSurfaceLight,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    state.question.answers.forEachIndexed { i, answer ->
                        AnswerRow(
                            text = answer,
                            selected = state.selectedIndex == i,
                            onClick = { viewModel.onAnswerSelected(i) }
                        )
                        if (i != state.question.answers.lastIndex) Spacer(Modifier.height(16.dp))
                    }

                    Spacer(Modifier.height(65.dp))

                    Button(
                        onClick = viewModel::onNextClicked,
                        enabled = state.isNextEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isNextEnabled) Purple else Grey,
                            contentColor = White,
                            disabledContainerColor = Grey,
                            disabledContentColor = White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text(
                            text = if (isLast) "ЗАВЕРШИТЬ" else "ДАЛЕЕ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "Вернуться к предыдущим вопросам нельзя",
                style = MaterialTheme.typography.bodyMedium,
                color = White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AnswerRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val border = if (selected) BorderStroke(2.dp, DarkPurple) else null

    Surface(
        color = Cream,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 0.dp,
        border = border,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selected) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(2.dp, Grey, CircleShape)
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = text,
                color = Black,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QuestionScreenPreview() {
    DailyQuizTheme {
        QuestionScreen(
            navController = rememberNavController(),
            index = 0
        )
    }
}