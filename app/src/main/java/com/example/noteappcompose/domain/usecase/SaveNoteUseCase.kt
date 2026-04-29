package com.example.noteappcompose.domain.usecase

import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.utils.NoteValidator

class SaveNoteUseCase(private val repository: NoteRepository, private val validator: NoteValidator)  {
    suspend operator fun invoke(note: Note) :Result<Unit>{
        val error = validator.noteValidator(note)
        if (error != null) return Result.failure(Exception(error))
        return if (note.id == 0) {
            repository.insertNote(note)
            Result.success(Unit)
        } else {
            repository.updateNote(note)
            Result.success(Unit)
        }
    }

}