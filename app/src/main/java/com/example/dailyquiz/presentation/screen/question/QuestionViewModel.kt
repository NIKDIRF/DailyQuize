package com.example.dailyquiz.presentation.screen.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquiz.data.session.QuizSessionManager
import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.domain.model.AttemptAnswer
import com.example.dailyquiz.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val session: QuizSessionManager,
    private val history: HistoryRepository
) : ViewModel() {

    data class QuestionUi(
        val index: Int,
        val total: Int,
        val title: String,
        val answers: List<String>,
        val correctIndex: Int
    )

    data class UiState(
        val question: QuestionUi,
        val selectedIndex: Int? = null,
        val reveal: RevealState = RevealState.None,
        val remainingSec: Int = 5 * 60,
        val timeUpDialog: Boolean = false,
        val enabled: Boolean = true
    ) {
        val isNextEnabled get() = selectedIndex != null && reveal == RevealState.None
    }

    enum class RevealState { None, Correct, Wrong }

    sealed interface UiEffect {
        data class NavigateNext(val nextIndex: Int) : UiEffect
        data class NavigateToResult(
            val correct: Int,
            val categoryId: Int?,
            val difficulty: String?
        ) : UiEffect
        object NavigateToStart : UiEffect
    }

    private val _effect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<UiEffect> = _effect

    private val _state = MutableStateFlow(createStateForIndex(0))
    val state: StateFlow<UiState> = _state

    private var timerJob: Job = viewModelScope.launch {
        while (isActive) {
            val leftMs = session.deadlineMillis - System.currentTimeMillis()
            val sec = (leftMs.coerceAtLeast(0L) / 1000L).toInt()
            _state.update { it.copy(remainingSec = sec) }
            if (leftMs <= 0L) {
                onTimeExpired()
                break
            }
            delay(1000L)
        }
    }

    fun bindIndex(index: Int) {
        _state.value = createStateForIndex(index)
    }

    private fun createStateForIndex(index: Int): UiState {
        val q = session.questions.getOrNull(index)
        return if (q != null) {
            UiState(
                question = QuestionUi(
                    index = q.index,
                    total = session.total(),
                    title = q.title,
                    answers = q.answers,
                    correctIndex = q.correctIndex
                ),
                selectedIndex = session.selections.getOrNull(index),
                remainingSec = ((session.deadlineMillis - System.currentTimeMillis())
                    .coerceAtLeast(0L) / 1000L).toInt()
            )
        } else {
            UiState(
                question = QuestionUi(
                    index = 0, total = 0, title = "", answers = emptyList(), correctIndex = -1
                ),
                selectedIndex = null
            )
        }
    }

    fun onAnswerSelected(index: Int) {
        val qIndex = _state.value.question.index
        session.pick(qIndex, index)
        _state.update { it.copy(selectedIndex = index) }
    }

    fun onNextClicked() {
        val s = _state.value
        val correct = (s.selectedIndex == s.question.correctIndex)
        _state.update {
            it.copy(
                reveal = if (correct) RevealState.Correct else RevealState.Wrong,
                enabled = false
            )
        }

        viewModelScope.launch {
            delay(2000)
            _state.update { it.copy(reveal = RevealState.None, enabled = true) }

            val isLast = s.question.index >= s.question.total - 1
            if (isLast) {
                finishNormally()
            } else {
                _effect.emit(UiEffect.NavigateNext(s.question.index + 1))
            }
        }
    }

    private suspend fun finishNormally() {
        val catId = session.categoryId
        val diff = session.difficultyApi

        timerJob.cancel()
        val correct = saveAttempt(clearSession = true)

        _effect.emit(UiEffect.NavigateToResult(correct, catId, diff))
    }

    private suspend fun saveAttempt(clearSession: Boolean): Int {
        if (!session.tryLockSaving()) {
            return session.correctCount()
        }

        val correct = session.correctCount()
        val attempt = Attempt(
            id = 0L,
            timestamp = System.currentTimeMillis(),
            correct = correct,
            total = session.total(),
            category = session.categoryId,
            difficulty = session.difficultyApi
        )
        val answers = session.questions.map { q ->
            AttemptAnswer(
                index = q.index,
                question = q.title,
                answers = q.answers,
                correctIndex = q.correctIndex,
                selectedIndex = session.selections[q.index]
            )
        }
        history.saveAttempt(attempt, answers)

        if (clearSession) session.clear()
        return correct
    }

    private fun onTimeExpired() {
        viewModelScope.launch {
            if (session.isAttemptSaved() || session.questions.isEmpty()) return@launch

            timerJob.cancel()

            session.fillUnansweredWithRandomWrong()
            saveAttempt(clearSession = false)

            _state.update { it.copy(timeUpDialog = true, enabled = false) }
        }
    }

    fun onDialogStartOver() {
        viewModelScope.launch {
            session.clear()
            _effect.emit(UiEffect.NavigateToStart)
        }
    }

    override fun onCleared() {
        timerJob.cancel()
        super.onCleared()
    }
}