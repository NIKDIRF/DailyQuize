package com.example.dailyquiz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.R
import com.example.dailyquiz.ui.theme.*

enum class AnswerVisualState { Default, Selected, Correct, Wrong }

@Composable
fun AnswerOptionRow(
    text: String,
    state: AnswerVisualState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor: Color = when (state) {
        AnswerVisualState.Default  -> Color.Transparent
        AnswerVisualState.Selected -> DarkPurple
        AnswerVisualState.Correct  -> Green
        AnswerVisualState.Wrong    -> Red
    }

    Surface(
        color = Cream,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 0.dp,
        border = if (borderColor == Color.Transparent) null else androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (state) {
                AnswerVisualState.Default -> {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(2.dp, Grey, CircleShape)
                    )
                }
                AnswerVisualState.Selected -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
                AnswerVisualState.Correct -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_correct),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }
                AnswerVisualState.Wrong -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_wrong),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = when (state) {
                    AnswerVisualState.Correct -> Green
                    AnswerVisualState.Wrong   -> Red
                    else -> Black
                },
                fontWeight = FontWeight.Normal
            )
        }
    }
}