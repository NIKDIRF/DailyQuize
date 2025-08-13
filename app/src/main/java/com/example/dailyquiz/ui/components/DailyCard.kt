package com.example.dailyquiz.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.ui.design.Dimens

@Composable
fun DailyCard(
    modifier: Modifier = Modifier,
    corner: Dp = Dimens.CardCornerLarge,
    contentPaddingH: Dp = 24.dp,
    contentPaddingV: Dp = 32.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large.copy(all = CornerSize(corner)),
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Column(Modifier.padding(horizontal = contentPaddingH).padding(top = contentPaddingV, bottom = contentPaddingV)) {
            content()
        }
    }
}