package com.example.noteappcompose.usecase

import app.cash.turbine.test
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.GetNotesUseCase
import com.example.noteappcompose.domain.model.Note
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetNotesUseCaseTest {
    private val repo: NoteRepository = mockk()
    private val useCase = GetNotesUseCase(repo)

    @Test
    fun `returns notes sorted pinned first`() = runTest {
        val notes = listOf(
            Note(id = 1, title = "Normal", content = "", isPinned = false),
            Note(id = 2, title = "Pinned", content = "", isPinned = true)
        )

        // 2 — every { } tells mockk what to return when called
        every { repo.getAllNotes() } returns flowOf(notes)

        useCase.getAllNotes().test {
            val result = awaitItem()
            assertEquals("Pinned", result.first().title)
            assert(result[0].isPinned)
            awaitComplete()

        }

    }

    @Test
    fun `empty list returns empty flow`() = runTest {
        every { repo.getAllNotes() } returns flowOf(emptyList())

        useCase.getAllNotes().test {
            assertEquals(0, awaitItem().size)
            awaitComplete()
        }

    }
}