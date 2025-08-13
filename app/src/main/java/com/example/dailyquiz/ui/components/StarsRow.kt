package com.example.dailyquiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import com.example.dailyquiz.R
import com.example.dailyquiz.ui.theme.Grey
import com.example.dailyquiz.ui.theme.Yellow

@Composable
fun StarsRow(
    total: Int,
    correct: Int,
    size: Dp = 40.dp,
    tintOn: Color = Yellow,
    tintOff: Color = Grey,
    modifier: Modifier = Modifier.fillMaxWidth(),
    arrangement: Arrangement.Horizontal = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
) {
    val safeTotal = total.coerceAtLeast(1)
    val safeCorrect = correct.coerceIn(0, safeTotal)

    Row(
        modifier = modifier,
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(safeTotal) { i ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (i < safeCorrect) tintOn else tintOff,
                modifier = Modifier.size(size)
            )
        }
    }
}