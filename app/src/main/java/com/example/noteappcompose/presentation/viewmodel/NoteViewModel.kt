package com.example.noteappcompose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.domain.usecase.DeleteNoteUseCase
import com.example.noteappcompose.domain.usecase.GetNotesUseCase
import com.example.noteappcompose.domain.usecase.SaveNoteUseCase
import com.example.noteappcompose.domain.usecase.SearchNotesUseCase
import com.example.noteappcompose.presentation.viewmodel.states.NoteUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
 import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class NoteViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<NoteUiState>(NoteUiState.Loading)
   open val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    open val notes: StateFlow<List<Note>> = _searchQuery
        .debounce(300) //waits for user to stop typing
        .flatMapLatest { query ->// ← triggered immediately
            _uiState.value = NoteUiState.Idle
            if (query.isBlank()){
                getNotesUseCase.getAllNotes()
            }
            else searchNotesUseCase(query)
        }
        .catch { _uiState.value = NoteUiState.Error(it.message ?: "Unknown error") }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000), emptyList()) // this 'll collect after 5000


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

   open fun saveNote(note: Note) {
       viewModelScope.launch(Dispatchers.IO) {
           _uiState.value = NoteUiState.Loading
           val result = saveNoteUseCase(note)
           _uiState.value =
               if (result.isSuccess)
                   NoteUiState.Saved
               else
                   NoteUiState.Error(result.exceptionOrNull()?.message ?: "Save failed")

           if (result.isSuccess) {
               delay(100)
               _uiState.value = NoteUiState.Idle
           }
       }
   }

    fun deleteNote(note: Note) = viewModelScope.launch {
        deleteNoteUseCase(note)
    }
}