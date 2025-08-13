package com.example.dailyquiz.presentation.screen.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dailyquiz.R
import com.example.dailyquiz.presentation.navigation.Routes
import com.example.dailyquiz.presentation.screen.quiz.QuizViewModel.UiEffect
import com.example.dailyquiz.presentation.screen.quiz.QuizViewModel.UiState
import com.example.dailyquiz.presentation.screen.quiz.components.FiltersCard
import com.example.dailyquiz.presentation.screen.quiz.components.HistoryChip
import com.example.dailyquiz.presentation.screen.quiz.components.WelcomeCard
import com.example.dailyquiz.ui.components.TopCenteredHeader
import com.example.dailyquiz.ui.design.Dimens
import com.example.dailyquiz.ui.theme.LightPurple
import com.example.dailyquiz.ui.theme.Purple

@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.NavigateToFirstQuestion -> navController.navigate(Routes.question(effect.index))
                UiEffect.NavigateToHistory -> navController.navigate(Routes.HISTORY)
                is UiEffect.ShowToast -> { }
            }
        }
    }

    QuizScreenContent(
        state = state,
        onHistoryClick = viewModel::onHistoryClick,
        onStartQuiz = viewModel::onStartQuizClicked,
        onRetry = viewModel::onRetry,
        onCategorySelected = viewModel::onCategorySelected,
        onDifficultySelected = viewModel::onDifficultySelected,
        onFiltersNext = viewModel::onFiltersNextClicked
    )
}

@Composable
private fun QuizScreenContent(
    state: UiState,
    onHistoryClick: () -> Unit,
    onStartQuiz: () -> Unit,
    onRetry: () -> Unit,
    onCategorySelected: (Int) -> Unit,
    onDifficultySelected: (String) -> Unit,
    onFiltersNext: () -> Unit
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val chipWidth = 120.dp
    val chipHeight = 40.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = Dimens.ScreenHorz),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is UiState.Filters -> {
                Spacer(Modifier.height(40.dp))
                TopCenteredHeader(logo = painterResource(R.drawable.logo_dailyquiz))
                Spacer(Modifier.height(40.dp))

                FiltersCard(
                    categoryId = state.categoryId,
                    difficultyApi = state.difficultyApi,
                    onCategorySelected = onCategorySelected,
                    onDifficultySelected = onDifficultySelected,
                    onNext = onFiltersNext
                )
            }

            is UiState.Loading -> {
                Spacer(Modifier.height(topInset + 52.dp))
                Box(Modifier.size(chipWidth, chipHeight))
                Spacer(Modifier.height(114.dp))
                TopCenteredHeader(logo = painterResource(R.drawable.logo_dailyquiz))
                Spacer(Modifier.height(120.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(56.dp),
                    color = LightPurple,
                    trackColor = Color.White,
                    strokeWidth = 4.dp
                )
            }

            UiState.Idle -> {
                Spacer(Modifier.height(topInset + 52.dp))
                HistoryChip(onClick = onHistoryClick, width = chipWidth, height = chipHeight)
                Spacer(Modifier.height(114.dp))
                TopCenteredHeader(logo = painterResource(R.drawable.logo_dailyquiz))
                Spacer(Modifier.height(40.dp))
                WelcomeCard(onPrimaryClick = onStartQuiz, showError = false)
            }

            is UiState.Error -> {
                Spacer(Modifier.height(topInset + 52.dp))
                HistoryChip(onClick = onHistoryClick, width = chipWidth, height = chipHeight)
                Spacer(Modifier.height(114.dp))
                TopCenteredHeader(logo = painterResource(R.drawable.logo_dailyquiz))
                Spacer(Modifier.height(40.dp))
                WelcomeCard(onPrimaryClick = onRetry, showError = true)
            }
        }
    }
}