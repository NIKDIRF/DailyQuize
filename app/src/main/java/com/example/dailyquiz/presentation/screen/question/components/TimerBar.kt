package com.example.dailyquiz.presentation.screen.question.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.ui.theme.Cream
import com.example.dailyquiz.ui.theme.DarkPurple
import kotlin.math.max
import kotlin.math.min

private fun format(sec: Int): String {
    val s = max(0, sec)
    val m = s / 60
    val ss = s % 60
    return "%02d:%02d".format(m, ss)
}

@Composable
fun TimerBar(
    remainingSec: Int,
    totalSec: Int = 5 * 60,
    modifier: Modifier = Modifier
) {
    val elapsed = min(totalSec, totalSec - remainingSec.coerceAtLeast(0))
    val progress = elapsed.toFloat() / totalSec

    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = format(elapsed), color = DarkPurple, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = format(totalSec), color = DarkPurple, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(shape = MaterialTheme.shapes.small),
            color = DarkPurple,
            trackColor = Cream
        )
    }
}