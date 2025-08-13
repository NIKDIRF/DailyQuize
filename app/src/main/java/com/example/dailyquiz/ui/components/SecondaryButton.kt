package com.example.dailyquiz.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.ui.design.Dimens.ButtonCorner
import com.example.dailyquiz.ui.design.Dimens.ButtonHeight
import com.example.dailyquiz.ui.theme.DarkPurple
import com.example.dailyquiz.ui.theme.White

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(ButtonHeight),
    elevation: Dp = 0.dp
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(ButtonCorner),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = DarkPurple,
            disabledContainerColor = White,
            disabledContentColor = DarkPurple.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}