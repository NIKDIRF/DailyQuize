package com.example.dailyquiz.data.session

import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class QuizSessionManager @Inject constructor() {

    data class Question(
        val index: Int,
        val title: String,
        val answers: List<String>,
        val correctIndex: Int
    )

    var questions: List<Question> = emptyList()
        private set

    val selections = mutableListOf<Int?>()

    var deadlineMillis: Long = 0L
        private set

    var categoryId: Int? = null
    var difficultyApi: String? = null

    private val attemptSavedOnce = AtomicBoolean(false)

    fun isAttemptSaved(): Boolean = attemptSavedOnce.get()

    fun tryLockSaving(): Boolean = attemptSavedOnce.compareAndSet(false, true)

    fun start(questions: List<Question>, deadlineMillis: Long) {
        this.questions = questions
        this.deadlineMillis = deadlineMillis
        selections.clear()
        repeat(questions.size) { selections += null }
        attemptSavedOnce.set(false)
    }

    fun pick(index: Int, answerIndex: Int) { selections[index] = answerIndex }

    fun fillUnansweredWithRandomWrong() {
        questions.forEachIndexed { idx, q ->
            if (selections[idx] == null) {
                val wrong = (q.answers.indices - q.correctIndex).random(Random)
                selections[idx] = wrong
            }
        }
    }

    fun correctCount(): Int =
        questions.indices.count { i -> selections[i] == questions[i].correctIndex }

    fun total(): Int = questions.size

    fun clear() {
        questions = emptyList()
        selections.clear()
        deadlineMillis = 0L
        categoryId = null
        difficultyApi = null
        attemptSavedOnce.set(false)
    }
}