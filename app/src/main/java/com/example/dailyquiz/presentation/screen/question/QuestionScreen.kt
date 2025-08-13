package com.example.dailyquiz.presentation.screen.question

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dailyquiz.R
import com.example.dailyquiz.presentation.navigation.Routes
import com.example.dailyquiz.ui.components.AnswerOptionRow
import com.example.dailyquiz.ui.components.AnswerVisualState
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.components.PrimaryButton
import com.example.dailyquiz.ui.components.TopCenteredHeader
import com.example.dailyquiz.ui.theme.*

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun QuestionScreen(
    navController: NavController,
    index: Int
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry(Routes.QUIZ_GRAPH) }
    val viewModel: QuestionViewModel = hiltViewModel(parentEntry)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(index) { viewModel.bindIndex(index) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is QuestionViewModel.UiEffect.NavigateNext -> {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                    navController.navigate(Routes.question(eff.nextIndex)) {
                        if (currentRoute != null) popUpTo(currentRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                is QuestionViewModel.UiEffect.NavigateToResult -> {
                    navController.navigate(
                        Routes.result(eff.correct, eff.categoryId, eff.difficulty)
                    ) {
                        popUpTo(Routes.QUIZ) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                QuestionViewModel.UiEffect.NavigateToStart -> {
                    navController.navigate(Routes.QUIZ) {
                        popUpTo(Routes.QUIZ_GRAPH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

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

            TopCenteredHeader(logo = painterResource(R.drawable.logo_dailyquiz))

            Spacer(Modifier.height(40.dp))

            DailyCard(modifier = Modifier.fillMaxWidth()) {
                com.example.dailyquiz.presentation.screen.question.components.TimerBar(
                    remainingSec = state.remainingSec,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Вопрос ${state.question.index + 1} из ${state.question.total}",
                    style = MaterialTheme.typography.titleMedium,
                    color = LightPurple,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = state.question.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = OnSurfaceLight,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                state.question.answers.forEachIndexed { i, answer ->
                    val visual = when {
                        state.reveal == QuestionViewModel.RevealState.Correct && i == state.selectedIndex -> AnswerVisualState.Correct
                        state.reveal == QuestionViewModel.RevealState.Wrong && i == state.selectedIndex ->
                            if (i == state.question.correctIndex) AnswerVisualState.Correct else AnswerVisualState.Wrong
                        state.selectedIndex == i -> AnswerVisualState.Selected
                        else -> AnswerVisualState.Default
                    }

                    AnswerOptionRow(
                        text = answer,
                        state = visual,
                        onClick = { if (state.enabled) viewModel.onAnswerSelected(i) }
                    )
                    if (i != state.question.answers.lastIndex) Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(24.dp))

                val isLast = state.question.index >= state.question.total - 1
                PrimaryButton(
                    text = if (isLast) "ЗАВЕРШИТЬ" else "ДАЛЕЕ",
                    onClick = { viewModel.onNextClicked() },
                    enabled = state.isNextEnabled,
                    modifier = Modifier.fillMaxWidth()
                )
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

    if (state.timeUpDialog) {
        com.example.dailyquiz.presentation.screen.question.components.TimeUpDialog(
            onStartOver = { viewModel.onDialogStartOver() }
        )
    }
}