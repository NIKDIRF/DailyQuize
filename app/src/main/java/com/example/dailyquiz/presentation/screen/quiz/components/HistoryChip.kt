package com.example.dailyquiz.presentation.screen.quiz.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.R

@Composable
fun HistoryChip(
    onClick: () -> Unit,
    width: Dp,
    height: Dp,
    corner: Dp = 24.dp,
    container: Color = Color.White,
    content: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
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
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.width(5.dp))
        Icon(
            painter = painterResource(R.drawable.ic_history),
            contentDescription = "История",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
    }
}