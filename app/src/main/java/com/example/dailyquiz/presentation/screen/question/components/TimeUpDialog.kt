package com.example.dailyquiz.presentation.screen.question.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.components.PrimaryButton
import com.example.dailyquiz.ui.theme.OnSurfaceLight

@Composable
fun TimeUpDialog(onStartOver: () -> Unit) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            DailyCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Время вышло!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnSurfaceLight,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Вы не успели завершить викторину. Попробуйте еще раз!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceLight,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        PrimaryButton(
                            text = "НАЧАТЬ ЗАНОВО",
                            onClick = onStartOver,
                            modifier = Modifier.width(260.dp)
                        )
                    }
                }
            }
        }
    }
}