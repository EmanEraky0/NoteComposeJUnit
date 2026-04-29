package com.example.noteappcompose.presentation.viewmodel.states

import com.example.noteappcompose.domain.model.Note

sealed class NoteUiState {
    data object Idle:NoteUiState()
    data object Loading : NoteUiState()
    data object Saved   : NoteUiState()
    data class Success(val notes: List<Note>) : NoteUiState()
    data class Error(val message: String)     : NoteUiState()
}