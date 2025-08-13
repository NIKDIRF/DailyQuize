package com.example.dailyquiz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dailyquiz.ui.theme.White

@Composable
fun TopCenteredHeader(
    title: String? = null,
    logo: Painter? = null,
    height: Dp = 40.dp
) {
    Box(
        modifier = Modifier
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        when {
            logo != null -> Image(painter = logo, contentDescription = null, modifier = Modifier.size(width = 180.dp, height = 40.dp))
            title != null -> Text(
                text = title,
                color = White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}