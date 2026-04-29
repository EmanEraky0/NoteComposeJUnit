package com.example.noteappcompose.domain.usecase

import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase (private val repository: NoteRepository){

    fun getAllNotes() : Flow<List<Note>> =
        repository.getAllNotes().
        map { note-> note.sortedByDescending { it.isPinned } }

}