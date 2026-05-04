package com.example.noteappcompose.utils

import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.DeleteNoteUseCase
import com.example.noteappcompose.domain.usecase.GetNotesUseCase
import com.example.noteappcompose.domain.usecase.SaveNoteUseCase
import com.example.noteappcompose.domain.usecase.SearchNotesUseCase
import com.example.noteappcompose.presentation.viewmodel.NoteViewModel
import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.presentation.viewmodel.states.NoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

// androidTest/.../utils/FakeNoteViewModel.kt

class FakeNoteViewModel(
    private val initialNotes: List<Note> = emptyList(),
    private val initialUiState: NoteUiState = NoteUiState.Idle,
    private val onSave: (Note) -> Unit = {}
) : NoteViewModel(
    GetNotesUseCase(FakeNoteRepository(initialNotes)),
    SearchNotesUseCase(FakeNoteRepository(initialNotes)),
    SaveNoteUseCase(FakeNoteRepository(initialNotes), NoteValidator()),
    DeleteNoteUseCase(FakeNoteRepository(initialNotes))
) {
    // override uiState with your own MutableStateFlow
    private val _fakeUiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<NoteUiState> = _fakeUiState.asStateFlow()

    // override notes with your own list
    private val _fakeNotes = MutableStateFlow(initialNotes)
    override val notes: StateFlow<List<Note>> = _fakeNotes.asStateFlow()

    override fun saveNote(note: Note) {
        onSave(note)
        _fakeUiState.value = NoteUiState.Saved
    }
}

// fake repository used internally
class FakeNoteRepository(
    private val notes: List<Note> = emptyList()
) : NoteRepository {
    override fun getAllNotes() = flowOf(notes)
    override fun searchNotes(query: String) = flowOf(
        notes.filter { it.title.contains(query, ignoreCase = true) }
    )
    override suspend fun getNoteById(id: Int) = notes.firstOrNull { it.id == id }
    override suspend fun insertNote(note: Note) = 1L
    override suspend fun updateNote(note: Note) {}
    override suspend fun deleteNote(note: Note) {}
}