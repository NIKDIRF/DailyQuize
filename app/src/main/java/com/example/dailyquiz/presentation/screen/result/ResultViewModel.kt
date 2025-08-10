package com.example.dailyquiz.presentation.screen.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val correct: Int = savedStateHandle.get<Int>("correct") ?: 0
    val total: Int = 5
}