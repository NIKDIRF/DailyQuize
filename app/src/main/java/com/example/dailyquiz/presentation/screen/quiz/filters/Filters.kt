package com.example.dailyquiz.presentation.screen.quiz.filters

data class Category(val id: Int, val title: String)

val CATEGORIES = listOf(
    Category(9,  "Общие знания"),
    Category(18, "Компьютеры"),
    Category(22, "География"),
    Category(23, "История"),
    Category(17, "Наука и природа"),
)

enum class Difficulty(val api: String, val title: String) {
    EASY("easy", "Низкая"),
    MEDIUM("medium", "Средняя"),
    HARD("hard", "Высокая")
}