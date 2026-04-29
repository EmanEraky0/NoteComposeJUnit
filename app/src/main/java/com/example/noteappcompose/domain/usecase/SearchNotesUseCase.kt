package com.example.noteappcompose.domain.usecase

import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(private val repository: NoteRepository) {

    operator fun invoke(query: String): Flow<List<Note>> =
    repository.searchNotes(query)

}