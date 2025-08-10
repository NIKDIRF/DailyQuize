package com.example.dailyquiz.presentation.screen.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquiz.domain.session.QuizSession
import com.example.dailyquiz.domain.usecase.SaveAttemptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val session: QuizSession,
    private val saveAttempt: SaveAttemptUseCase
) : ViewModel() {

    data class QuestionUi(
        val index: Int,
        val total: Int,
        val title: String,
        val answers: List<String>,
        val correctAnswerIndex: Int
    )

    data class UiState(
        val question: QuestionUi,
        val selectedIndex: Int? = null
    ) {
        val isNextEnabled: Boolean get() = selectedIndex != null
    }

    sealed interface UiEffect {
        data class NavigateNext(val nextIndex: Int) : UiEffect
        data class NavigateToResult(val correct: Int) : UiEffect
    }

    private val _effect = MutableSharedFlow<UiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<UiEffect> = _effect

    private val total: Int
        get() = session.questions.size.takeIf { it > 0 } ?: 5

    private var selections: MutableList<Int?> = mutableListOf()

    private var correctCount = 0
    private var isSaved = false

    private fun ensureSelectionsSized() {
        if (selections.size != total) {
            selections = MutableList(total) { null }
        }
    }

    private fun mapToUi(index: Int): QuestionUi {
        val q = session.questions.getOrNull(index)
        return if (q != null) {
            QuestionUi(
                index = index,
                total = session.questions.size,
                title = q.text,
                answers = q.answers,
                correctAnswerIndex = q.correctIndex
            )
        } else {
            QuestionUi(
                index = index.coerceAtLeast(0),
                total = total,
                title = "Как переводится слово «apple»?",
                answers = listOf("Груша", "Яблоко", "Апельсин", "Ананас"),
                correctAnswerIndex = 1
            )
        }
    }

    private val _state = MutableStateFlow(
        UiState(question = mapToUi(0), selectedIndex = null)
    )
    val state: StateFlow<UiState> = _state

    fun bindIndex(index: Int) {
        ensureSelectionsSized()
        val safe = index.coerceIn(0, total - 1)
        _state.value = UiState(
            question = mapToUi(safe),
            selectedIndex = selections.getOrNull(safe)
        )
    }

    fun onAnswerSelected(index: Int) {
        val qIndex = _state.value.question.index
        ensureSelectionsSized()
        selections[qIndex] = index
        _state.value = _state.value.copy(selectedIndex = index)
    }

    fun onNextClicked() {
        val s = _state.value
        val isCorrect = s.selectedIndex == s.question.correctAnswerIndex
        if (isCorrect) correctCount++

        viewModelScope.launch {
            val isLast = s.question.index >= s.question.total - 1
            if (isLast) {
                if (!isSaved) {
                    isSaved = true
                    val answers = session.buildAnswers(selections)
                    saveAttempt(
                        correct = correctCount,
                        total = s.question.total,
                        answers = answers
                    )
                    session.clear()
                }
                _effect.emit(UiEffect.NavigateToResult(correct = correctCount))
                correctCount = 0
                selections.fill(null)
                isSaved = false
            } else {
                _state.value = _state.value.copy(selectedIndex = null)
                _effect.emit(UiEffect.NavigateNext(s.question.index + 1))
            }
        }
    }
}