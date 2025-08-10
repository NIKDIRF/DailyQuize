package com.example.dailyquiz.data.mapper

import androidx.core.text.HtmlCompat
import com.example.dailyquiz.data.remote.dto.QuestionDto
import com.example.dailyquiz.domain.model.QuizQuestion
import kotlin.random.Random

private fun decodeHtml(s: String): String =
    HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

fun QuestionDto.toDomain(id: Int): QuizQuestion {
    val decodedCorrect = decodeHtml(correctAnswer)
    val decodedIncorrect = incorrectAnswers.map(::decodeHtml)
    val options = (decodedIncorrect + decodedCorrect).shuffled(Random(id))
    val correctIndex = options.indexOf(decodedCorrect)

    return QuizQuestion(
        id = id,
        text = decodeHtml(question),
        answers = options,
        correctIndex = correctIndex
    )
}