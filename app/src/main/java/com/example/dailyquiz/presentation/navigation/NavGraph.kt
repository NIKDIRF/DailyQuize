package com.example.dailyquiz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
        startDestination = Routes.QUIZ_GRAPH
    ) {
        navigation(
            startDestination = Routes.QUIZ,
            route = Routes.QUIZ_GRAPH
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

            // ---------- Result с 3 аргументами ----------
            composable(
                route = Routes.RESULT,
                arguments = listOf(
                    navArgument("correct") { type = NavType.IntType },
                    navArgument("category") { type = NavType.IntType },
                    navArgument("difficulty") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val correct = backStackEntry.arguments?.getInt("correct") ?: 0
                val rawCategory = backStackEntry.arguments?.getInt("category") ?: -1
                val rawDifficulty = backStackEntry.arguments?.getString("difficulty") ?: "none"

                val categoryId = if (rawCategory == -1) null else rawCategory
                val difficulty = if (rawDifficulty == "none") null else rawDifficulty

                ResultScreen(
                    navController = navController,
                    correct = correct,
                    categoryId = categoryId,
                    difficultyApi = difficulty
                )
            }
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