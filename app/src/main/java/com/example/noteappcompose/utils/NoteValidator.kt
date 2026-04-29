package com.example.noteappcompose.utils

import com.example.noteappcompose.domain.model.Note

class NoteValidator {

    fun noteValidator(note: Note): String? = when {
        note.title.isBlank()        -> "Title cannot be empty"
        note.title.length > 100     -> "Title cannot exceed 100 characters"
        note.content.length > 5000  -> "Content cannot exceed 5000 characters"
        else                        -> null
    }
}