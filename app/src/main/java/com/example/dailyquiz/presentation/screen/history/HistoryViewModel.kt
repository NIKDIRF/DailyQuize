package com.example.dailyquiz.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyquiz.domain.model.Attempt
import com.example.dailyquiz.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: HistoryRepository
) : ViewModel() {

    val attempts: StateFlow<List<Attempt>> =
        repo.observeAttempts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    sealed interface UiEffect {
        data object ShowDeletedDialog : UiEffect
    }

    private val _effect = MutableSharedFlow<UiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onDeleteAttempt(id: Long) {
        viewModelScope.launch {
            repo.deleteAttempt(id)
            _effect.emit(UiEffect.ShowDeletedDialog)
        }
    }
}