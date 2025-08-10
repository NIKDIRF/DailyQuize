package com.example.dailyquiz.presentation.screen.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquiz.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttemptDetailsViewModel @Inject constructor(
    private val repo: HistoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    data class State(
        val correct: Int = 0,
        val total: Int = 0,
        val questions: List<DetailedQuestionUi> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val attemptId: Long = checkNotNull(savedStateHandle["attemptId"])

    init {
        viewModelScope.launch {
            repo.observeAttemptDetails(attemptId).collect { details ->
                if (details != null) {
                    val qUi = details.answers
                        .sortedBy { it.index }
                        .map { a ->
                            DetailedQuestionUi(
                                index = a.index,
                                total = details.attempt.total,
                                title = a.question,
                                answers = a.answers,
                                correctIndex = a.correctIndex,
                                selectedIndex = a.selectedIndex
                            )
                        }

                    _state.update {
                        it.copy(
                            correct = details.attempt.correct,
                            total = details.attempt.total,
                            questions = qUi
                        )
                    }
                }
            }
        }
    }
}