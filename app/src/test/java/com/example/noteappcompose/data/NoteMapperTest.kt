package com.example.noteappcompose.data

import com.example.noteappcompose.data.local.entity.NoteEntity
import com.example.noteappcompose.data.mapper.toDomain
import com.example.noteappcompose.data.mapper.toEntity
import junit.framework.TestCase.assertEquals
import com.example.noteappcompose.domain.model.Note
import kotlin.test.Test

class NoteMapperTest {
    @Test
    fun `NoteEntity maps to Note correctly`() {
        val entity = NoteEntity(
            id = 1,
            title = "Title",
            content = "Content",
            createdAt = 1234567890,
            isPinned = false
        )
        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
    }

    @Test
    fun `Note maps to NoteEntity correctly`() {
        val note = Note(
            id = 1,
            title = "Title",
            content = "Content",
            createdAt = 1234567890,
            isPinned = false
        )
        val entity = note.toEntity()
        assertEquals(note.id, entity.id)
        assertEquals(note.title, entity.title)
    }

    @Test
    fun `round-trip mapping preserves all fields`() {
        val original = Note(
            id = 1,
            title = "Title",
            content = "Content",
            createdAt = 1234567890,
            isPinned = false
        )
        val round = original.toEntity().toDomain()
        assertEquals(original, round)

    }
}