package com.example.dailyquiz.presentation.screen.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.R
import com.example.dailyquiz.presentation.screen.quiz.QuizViewModel.UiState
import com.example.dailyquiz.ui.theme.DailyQuizTheme

@Composable
fun QuizWelcomeContent(
    state: UiState,
    onHistoryClick: () -> Unit,
    onStartQuizClick: () -> Unit,
    onRetry: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val onCard = MaterialTheme.colorScheme.onSurface

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val isLoading = state is UiState.Loading
    val isError = state is UiState.Error

    val chipWidth = 120.dp
    val chipHeight = 40.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(topInset + 52.dp))

            if (isLoading) {
                Box(Modifier.size(chipWidth, chipHeight))
            } else {
                HistoryChip(
                    onClick = onHistoryClick,
                    enabled = true,
                    width = chipWidth,
                    height = chipHeight,
                    corner = 24.dp,
                    container = Color.White,
                    content = primary
                )
            }

            Spacer(Modifier.height(114.dp))

            Logo(width = 300.dp, height = 67.67.dp)

            when (state) {
                UiState.Loading -> {
                    Spacer(Modifier.height(120.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = onPrimary
                    )
                }

                UiState.Idle, is UiState.Error -> {
                    Spacer(Modifier.height(40.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(46.dp),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 40.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Добро пожаловать\nв DailyQuiz!",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = onCard,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(40.dp))

                            if (state is UiState.Error) {
                                StartButton(
                                    onClick = onRetry,
                                    primary = primary,
                                    onPrimary = onPrimary,
                                    enabled = true
                                )
                            } else {
                                StartButton(
                                    onClick = onStartQuizClick,
                                    primary = primary,
                                    onPrimary = onPrimary,
                                    enabled = true
                                )
                            }
                        }
                    }

                    if (isError) {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Ошибка! Попробуйте ещё раз",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartButton(
    onClick: () -> Unit,
    primary: Color,
    onPrimary: Color,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = primary,
            contentColor = onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = "НАЧАТЬ ВИКТОРИНУ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HistoryChip(
    onClick: () -> Unit,
    enabled: Boolean,
    width: Dp,
    height: Dp,
    corner: Dp,
    container: Color,
    content: Color
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(corner),
        modifier = Modifier.size(width, height),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.6f),
            disabledContentColor = content.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Text(
            text = "История",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.ic_history),
            contentDescription = "История",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun Logo(width: Dp, height: Dp) {
    Image(
        painter = painterResource(R.drawable.logo_dailyquiz),
        contentDescription = "DAILYQUIZ",
        modifier = Modifier.size(width = width, height = height),
        contentScale = ContentScale.Fit
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "Welcome / Idle")
@Composable
private fun PreviewWelcomeIdle() {
    DailyQuizTheme {
        QuizWelcomeContent(
            state = UiState.Idle,
            onHistoryClick = {},
            onStartQuizClick = {},
            onRetry = {}
        )
    }
}