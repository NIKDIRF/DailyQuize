package com.example.dailyquiz.presentation.screen.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dailyquiz.presentation.navigation.Routes
import com.example.dailyquiz.presentation.screen.quiz.QuizViewModel.UiEffect

@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.NavigateToFirstQuestion ->
                    navController.navigate(Routes.question(effect.index))
                UiEffect.NavigateToHistory ->
                    navController.navigate(Routes.HISTORY)
                is UiEffect.ShowToast -> { }
            }
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            QuizWelcomeContent(
                state = state,
                onHistoryClick = viewModel::onHistoryClick,
                onStartQuizClick = viewModel::onStartQuizClicked,
                onRetry = viewModel::onRetry
            )
        }
    }
}