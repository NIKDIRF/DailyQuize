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

sealed interface AnswerState {
    data object Unselected : AnswerState
    data object Selected : AnswerState
    data object Correct : AnswerState
    data object Wrong : AnswerState
}

@Composable
fun AnswerOption(
    text: String,
    state: AnswerState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val outlineColor = when (state) {
        AnswerState.Selected -> DarkPurple
        AnswerState.Correct  -> Green
        AnswerState.Wrong    -> Red
        else                 -> Color.Transparent
    }

    Surface(
        color = Cream,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick)
            .border(
                width = if (outlineColor == Color.Transparent) 0.dp else 2.dp,
                color = outlineColor,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (state) {
                AnswerState.Unselected -> Box(
                    modifier = Modifier.size(20.dp).border(2.dp, Grey, CircleShape)
                )
                AnswerState.Selected -> Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                AnswerState.Correct -> Icon(
                    painter = painterResource(R.drawable.ic_correct),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                AnswerState.Wrong -> Icon(
                    painter = painterResource(R.drawable.ic_wrong),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(8.dp))
            Text(
                text = text,
                color = when (state) {
                    AnswerState.Correct -> Green
                    AnswerState.Wrong   -> Red
                    else                -> Black
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}