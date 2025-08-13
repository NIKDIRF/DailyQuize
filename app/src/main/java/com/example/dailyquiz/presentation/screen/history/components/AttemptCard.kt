package com.example.dailyquiz.presentation.screen.history.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.R
import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.ui.theme.Grey
import com.example.dailyquiz.ui.theme.OnSurfaceLight
import com.example.dailyquiz.ui.theme.SurfaceLight
import com.example.dailyquiz.ui.theme.Yellow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttemptCard(
    indexTitle: String,
    attempt: Attempt,
    onClick: () -> Unit,
    onLongPress: (CardBounds) -> Unit
) {
    var myOffset by remember { mutableStateOf(IntOffset.Zero) }
    var mySize by remember { mutableStateOf(IntSize.Zero) }

    Surface(
        color = SurfaceLight,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coords ->
                val pos = coords.positionInRoot()
                myOffset = IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
                mySize = coords.size
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = { onLongPress(CardBounds(myOffset, mySize)) }
            )
    ) {
        AttemptCardContent(indexTitle = indexTitle, attempt = attempt)
    }
}

@Composable
private fun AttemptCardContent(indexTitle: String, attempt: Attempt) {
    Box(Modifier.padding(24.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = indexTitle,
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceLight
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = formatDate(attempt.timestamp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceLight
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    StarsRow(correct = attempt.correct, total = attempt.total)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = formatTime(attempt.timestamp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceLight
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            val categoryName = attempt.category?.let { id -> resolveCategoryTitle(id) }
            val difficultyName = attempt.difficulty?.let { d ->
                val clean = d.trim()
                if (clean.isNotEmpty()) resolveDifficultyTitle(clean) else null
            }

            if (categoryName != null || difficultyName != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    categoryName?.let {
                        Text(
                            text = "Категория: $it",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = OnSurfaceLight
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                    difficultyName?.let {
                        Text(
                            text = "Сложность: $it",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = OnSurfaceLight
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StarsRow(correct: Int, total: Int) {
    val safeTotal = total.coerceAtLeast(1)
    val safeCorrect = correct.coerceIn(0, safeTotal)
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(safeTotal) { i ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (i < safeCorrect) Yellow else Grey,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private val ruLocale = Locale("ru")
private val dateFormat by lazy {
    SimpleDateFormat("d MMMM", ruLocale).apply { timeZone = TimeZone.getDefault() }
}
private val timeFormat by lazy {
    SimpleDateFormat("HH:mm", ruLocale).apply { timeZone = TimeZone.getDefault() }
}
private fun formatDate(ts: Long): String = dateFormat.format(Date(ts))
private fun formatTime(ts: Long): String = timeFormat.format(Date(ts))

private fun resolveDifficultyTitle(d: String): String = when (d.lowercase()) {
    "easy" -> "Низкая"
    "medium" -> "Средняя"
    "hard" -> "Высокая"
    else -> d
}

private val categoryMap: Map<Int, String> = mapOf(
    9 to "Общие знания",
    17 to "Наука и природа",
    18 to "Компьютеры",
    22 to "География",
    23 to "История"
)

private fun resolveCategoryTitle(id: Int): String =
    categoryMap[id] ?: "Категория #$id"