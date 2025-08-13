package com.example.dailyquiz.presentation.screen.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dailyquiz.R
import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.presentation.navigation.Routes
import com.example.dailyquiz.presentation.screen.history.components.AttemptCard
import com.example.dailyquiz.presentation.screen.history.components.CardBounds
import com.example.dailyquiz.presentation.screen.history.components.EmptyHistoryCard
import com.example.dailyquiz.ui.theme.*
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val attempts by viewModel.attempts.collectAsStateWithLifecycle()
    var showDeletedDialog by remember { mutableStateOf(false) }

    var selected by remember { mutableStateOf<Attempt?>(null) }
    var selectedBounds by remember { mutableStateOf<CardBounds?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                HistoryViewModel.UiEffect.ShowDeletedDialog -> showDeletedDialog = true
            }
        }
    }

    val chipWidth = 230.dp
    val chipHeight = 48.dp
    val chipRadius = 12.dp
    val chipGap = 7.dp

    var overlayOffsetInRoot by remember { mutableStateOf(IntOffset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            Text(
                text = "История",
                style = MaterialTheme.typography.headlineMedium,
                color = White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            if (attempts.isEmpty()) {
                EmptyHistoryCard(
                    onStart = { navController.navigate(Routes.QUIZ) { launchSingleTop = true } }
                )

                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.logo_dailyquiz),
                    contentDescription = "DAILYQUIZ",
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(width = 180.dp, height = 40.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(attempts, key = { _, a -> a.id }) { index, item ->
                        AttemptCard(
                            indexTitle = "Quiz ${attempts.size - index}",
                            attempt = item,
                            onClick = { navController.navigate(Routes.attempt(item.id)) },
                            onLongPress = { bounds ->
                                selected = item
                                selectedBounds = bounds
                            }
                        )
                    }
                }
            }
        }

        val target = selected
        val bounds = selectedBounds
        if (target != null && bounds != null) {
            val corner = 28.dp
            val radiusPx = with(LocalDensity.current) { corner.toPx() }

            val holeTopLeft = IntOffset(
                x = bounds.offset.x - overlayOffsetInRoot.x,
                y = bounds.offset.y - overlayOffsetInRoot.y
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coords ->
                        val p = coords.positionInRoot()
                        overlayOffsetInRoot = IntOffset(p.x.roundToInt(), p.y.roundToInt())
                    }
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawWithContent {
                        drawContent()
                        drawRect(Color.Black.copy(alpha = 0.45f))
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(holeTopLeft.x.toFloat(), holeTopLeft.y.toFloat()),
                            size = Size(bounds.size.width.toFloat(), bounds.size.height.toFloat()),
                            cornerRadius = CornerRadius(radiusPx, radiusPx),
                            blendMode = BlendMode.Clear
                        )
                    }
                    .combinedClickable(
                        onClick = { selected = null; selectedBounds = null },
                        onLongClick = { }
                    )
            )

            val chipWpx = with(LocalDensity.current) { chipWidth.toPx().toInt() }
            val chipHpx = with(LocalDensity.current) { chipHeight.toPx().toInt() }
            val gapPx = with(LocalDensity.current) { chipGap.toPx().toInt() }

            val chipX = holeTopLeft.x + bounds.size.width - chipWpx
            val chipY = holeTopLeft.y - chipHpx - gapPx

            Surface(
                shape = RoundedCornerShape(chipRadius),
                color = SurfaceLight,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .offset { IntOffset(chipX, chipY) }
                    .size(chipWidth, chipHeight)
                    .combinedClickable(
                        onClick = {
                            selected = null
                            selectedBounds = null
                            viewModel.onDeleteAttempt(target.id)
                        },
                        onLongClick = {}
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Удалить",
                        tint = OnSurfaceLight
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Удалить",
                        style = MaterialTheme.typography.titleMedium,
                        color = OnSurfaceLight
                    )
                }
            }
        }
    }

    if (showDeletedDialog) {
        Dialog(
            onDismissRequest = { showDeletedDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = White,
                    shadowElevation = 12.dp,
                    modifier = Modifier.size(width = 340.dp, height = 170.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Попытка удалена",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = OnSurfaceLight
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Вы можете пройти викторину снова,\nкогда будете готовы.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = OnSurfaceLight
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(onClick = { showDeletedDialog = false }) {
                                Text(
                                    text = "ЗАКРЫТЬ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Purple
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "History / пусто")
@Composable
private fun PreviewHistoryEmpty() {
    DailyQuizTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(40.dp))
                Text(
                    text = "История",
                    style = MaterialTheme.typography.headlineMedium,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(40.dp))

                EmptyHistoryCard(onStart = {})

                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.logo_dailyquiz),
                    contentDescription = "DAILYQUIZ",
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(width = 180.dp, height = 40.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "History / список")
@Composable
private fun PreviewHistoryList() {
    DailyQuizTheme {
        val samples = listOf(
            Attempt(id = 1, timestamp = 1720524180000, correct = 1, total = 5),
            Attempt(id = 2, timestamp = 1720610580000, correct = 3, total = 5),
            Attempt(id = 3, timestamp = 1720696980000, correct = 4, total = 5),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(40.dp))
                Text(
                    text = "История",
                    style = MaterialTheme.typography.headlineMedium,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(40.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(samples, key = { _, a -> a.id }) { index, item ->
                        AttemptCard(
                            indexTitle = "Quiz ${samples.size - index}",
                            attempt = item,
                            onClick = {},
                            onLongPress = {}
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "History / диалог после удаления")
@Composable
private fun PreviewDeletedDialog() {
    DailyQuizTheme {
        Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = White,
                    shadowElevation = 12.dp,
                    modifier = Modifier.size(width = 340.dp, height = 170.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Попытка удалена",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = OnSurfaceLight
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Вы можете пройти викторину снова,\nкогда будете готовы.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = OnSurfaceLight
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(onClick = {}) {
                                Text(
                                    text = "ЗАКРЫТЬ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Purple
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}