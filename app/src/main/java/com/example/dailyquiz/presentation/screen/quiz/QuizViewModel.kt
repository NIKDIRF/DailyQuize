package com.example.dailyquiz.presentation.screen.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquiz.domain.session.QuizSession
import com.example.dailyquiz.domain.usecase.StartQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val startQuiz: StartQuizUseCase,
    private val session: QuizSession
) : ViewModel() {

    sealed interface UiState {
        data object Idle : UiState
        data object Loading : UiState
        data class Error(val message: String) : UiState
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    sealed interface UiEffect {
        data class NavigateToFirstQuestion(val index: Int = 0) : UiEffect
        data object NavigateToHistory : UiEffect
        data class ShowToast(val message: String) : UiEffect
    }

    private val _effect = MutableSharedFlow<UiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onStartQuizClicked() {
        if (_state.value is UiState.Loading) return
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val questions = startQuiz(category = null, difficulty = null)
                if (questions.isEmpty()) {
                    throw IllegalStateException("Сервис вернул пустой список вопросов.")
                }
                session.set(questions)
                _state.value = UiState.Idle
                _effect.emit(UiEffect.NavigateToFirstQuestion(0))
            } catch (t: Throwable) {
                _state.value = UiState.Error(
                    t.message ?: "Ошибка загрузки. Проверьте соединение и повторите."
                )
            }
        }
    }

    fun onHistoryClick() {
        viewModelScope.launch { _effect.emit(UiEffect.NavigateToHistory) }
    }

    fun onRetry() {
        _state.value = UiState.Idle
    }
}