package com.example.noteappcompose.utils
import com.example.noteappcompose.domain.model.Note
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NoteValidatorTest {
    private val validator = NoteValidator()

    @Test
    fun `valid note returns null`() {
        val note= Note(title = "title", content = "content")
        val actualError = validator.noteValidator(note)
        assertNull(null,actualError)
    }

    @Test
    fun `blank title returns error`() {
        val note= Note(title = "", content = "content")
        val actualError = validator.noteValidator(note)
        val expectedError = "Title cannot be empty"
        assertEquals(expectedError,actualError)
    }

    @Test
    fun `title longer than 100 characters returns error`(){
        val note= Note(title = "Hello user".repeat(50), content = "content")
        val actualError = validator.noteValidator(note)
        val expectedError = "Title cannot  exceed 100 characters"
        assertEquals(expectedError,actualError)
    }

    @Test
    fun `content longer than 5000 characters returns error`(){
        val note= Note(title = "Hello user", content = "content".repeat(5000))
        val actualError = validator.noteValidator(note)
        val expectedError = "Content cannot exceed 5000 characters"
        assertEquals(expectedError,actualError)
    }
}