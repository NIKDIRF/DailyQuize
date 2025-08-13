package com.example.dailyquiz.presentation.screen.quiz.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailyquiz.presentation.screen.quiz.filters.CATEGORIES
import com.example.dailyquiz.presentation.screen.quiz.filters.Difficulty
import com.example.dailyquiz.ui.components.DailyCard
import com.example.dailyquiz.ui.components.PrimaryButton
import com.example.dailyquiz.ui.theme.Black
import com.example.dailyquiz.ui.theme.Cream
import com.example.dailyquiz.ui.theme.DarkPurple

@Composable
fun FiltersCard(
    categoryId: Int?,
    difficultyApi: String?,
    onCategorySelected: (Int) -> Unit,
    onDifficultySelected: (String) -> Unit,
    onNext: () -> Unit
) {
    val difficulties = Difficulty.values()

    var catExpanded by remember { mutableStateOf(false) }
    var diffExpanded by remember { mutableStateOf(false) }

    val nextEnabled = categoryId != null && difficultyApi != null

    val selectedCategoryTitle = CATEGORIES.firstOrNull { it.id == categoryId }?.title
    val selectedDifficultyTitle = difficulties.firstOrNull { it.api == difficultyApi }?.title

    DailyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Почти готовы!",
            modifier = Modifier.fillMaxWidth(),
            color = Black,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Осталось выбрать категорию\nи сложность викторины.",
            modifier = Modifier.fillMaxWidth(),
            color = Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        // до первой плашки 40dp
        Spacer(Modifier.height(40.dp))

        ExpandableFilterPanel(
            title = "Категория",
            selectedText = selectedCategoryTitle,
            expanded = catExpanded,
            onHeaderClick = {
                catExpanded = !catExpanded
                if (diffExpanded) diffExpanded = false
            },
            options = CATEGORIES.map { it.title },
            onOptionClick = { index ->
                onCategorySelected(CATEGORIES[index].id)
                catExpanded = false
            }
        )

        Spacer(Modifier.height(16.dp))

        ExpandableFilterPanel(
            title = "Сложность",
            selectedText = selectedDifficultyTitle,
            expanded = diffExpanded,
            onHeaderClick = {
                diffExpanded = !diffExpanded
                if (catExpanded) catExpanded = false
            },
            options = difficulties.map { it.title },
            onOptionClick = { index ->
                onDifficultySelected(difficulties[index].api)
                diffExpanded = false
            }
        )

        Spacer(Modifier.height(40.dp))

        PrimaryButton(
            text = "ДАЛЕЕ",
            onClick = onNext,
            enabled = nextEnabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ExpandableFilterPanel(
    title: String,
    selectedText: String?,
    expanded: Boolean,
    onHeaderClick: () -> Unit,
    options: List<String>,
    onOptionClick: (Int) -> Unit
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrowRotation"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Cream, shape = RoundedCornerShape(16.dp))
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 14.5.dp,
                    bottom = 14.5.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onHeaderClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedText ?: title,
                    color = if (selectedText != null) Black else DarkPurple,
                    fontSize = 16.sp,
                    fontWeight = if (selectedText != null) FontWeight.Normal else FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = null,
                    tint = DarkPurple,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(arrowRotation)
                )
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))
                options.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionClick(index) }
                    ) {
                        Text(
                            text = item,
                            color = Black,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        )
                    }
                    if (index != options.lastIndex) Spacer(Modifier.height(12.dp))
                }
            }
        }

        if (selectedText != null) {
            Text(
                text = title,
                color = DarkPurple,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp)
                    .offset(y = (-10).dp)
            )
        }
    }
}