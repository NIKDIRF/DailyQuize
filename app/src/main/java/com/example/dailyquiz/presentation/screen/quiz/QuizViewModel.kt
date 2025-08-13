package com.example.dailyquiz.presentation.screen.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquiz.data.session.QuizSessionManager
import com.example.dailyquiz.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repo: QuizRepository,
    private val session: QuizSessionManager
) : ViewModel() {

    sealed interface UiState {
        data object Idle : UiState
        data object Loading : UiState

        data class Filters(
            val categoryId: Int? = null,
            val difficultyApi: String? = null
        ) : UiState

        data class Error(val message: String) : UiState
    }

    sealed interface UiEffect {
        data class NavigateToFirstQuestion(val index: Int = 0) : UiEffect
        data object NavigateToHistory : UiEffect
        data class ShowToast(val message: String) : UiEffect
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    private val _effect = MutableSharedFlow<UiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onStartQuizClicked() {
        if (_state.value is UiState.Loading) return
        _state.value = UiState.Filters()
    }

    fun onCategorySelected(id: Int) {
        val s = _state.value
        if (s is UiState.Filters) _state.value = s.copy(categoryId = id)
    }

    fun onDifficultySelected(api: String) {
        val s = _state.value
        if (s is UiState.Filters) _state.value = s.copy(difficultyApi = api)
    }

    fun onFiltersNextClicked() {
        val s = _state.value as? UiState.Filters ?: return
        if (s.categoryId == null || s.difficultyApi == null) return

        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val apiQuestions = repo.startQuiz(
                    amount = 5,
                    type = "multiple",
                    category = s.categoryId,
                    difficulty = s.difficultyApi
                )

                session.categoryId = s.categoryId
                session.difficultyApi = s.difficultyApi

                val sessionQuestions = apiQuestions.mapIndexed { i, q ->
                    QuizSessionManager.Question(
                        index = i,
                        title = q.text,
                        answers = q.answers,
                        correctIndex = q.correctIndex
                    )
                }

                val deadlineMillis = System.currentTimeMillis() + 5 * 60_000
                session.start(sessionQuestions, deadlineMillis)

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