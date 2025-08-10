package com.example.dailyquiz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailyquiz.presentation.screen.history.HistoryScreen
import com.example.dailyquiz.presentation.screen.question.QuestionScreen
import com.example.dailyquiz.presentation.screen.quiz.QuizScreen
import com.example.dailyquiz.presentation.screen.result.ResultScreen
import com.example.dailyquiz.presentation.screen.review.AttemptDetailsScreen
import com.example.dailyquiz.presentation.screen.review.AttemptDetailsViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.QUIZ
    ) {
        composable(Routes.QUIZ) {
            QuizScreen(navController = navController)
        }

        composable(
            route = Routes.QUESTION,
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            QuestionScreen(navController = navController, index = index)
        }

        composable(
            route = Routes.RESULT,
            arguments = listOf(navArgument("correct") { type = NavType.IntType })
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            ResultScreen(navController = navController, correct = correct)
        }

        composable(Routes.HISTORY) {
            HistoryScreen(navController = navController)
        }

        composable(
            route = Routes.ATTEMPT,
            arguments = listOf(navArgument("attemptId") { type = NavType.LongType })
        ) {
            val vm: AttemptDetailsViewModel = hiltViewModel()
            val state = vm.state.collectAsStateWithLifecycle().value

            AttemptDetailsScreen(
                navController = navController,
                correct = state.correct,
                total = state.total,
                questions = state.questions,
                onStartOver = {
                    navController.navigate(Routes.QUIZ) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}