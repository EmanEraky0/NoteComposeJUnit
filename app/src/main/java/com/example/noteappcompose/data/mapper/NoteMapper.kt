package com.example.noteappcompose.data.mapper

import com.example.noteappcompose.data.local.entity.NoteEntity
import com.example.noteappcompose.domain.model.Note


fun NoteEntity.toDomain() = Note(
    id = id, title = title, content = content,
    createdAt = createdAt, isPinned = isPinned
)

fun Note.toEntity() = NoteEntity(
    id = id, title = title, content = content,
    createdAt = createdAt, isPinned = isPinned
)

