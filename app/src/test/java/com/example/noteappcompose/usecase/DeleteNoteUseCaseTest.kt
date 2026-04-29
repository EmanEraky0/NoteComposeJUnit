package com.example.noteappcompose.usecase

import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.DeleteNoteUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteNoteUseCaseTest {
    private val repo: NoteRepository = mockk()
    private val useCase = DeleteNoteUseCase(repo)
    private val note = Note(id = 5, title = "Normal", content = "testDelete", isPinned = false)

    @Test
    fun `valid note is deleted`() = runTest {

        coEvery { repo.deleteNote(note) } just Runs

        useCase.invoke(note)
        coVerify(exactly = 1) { repo.deleteNote(note) }

    }

    @Test
    fun `deleteNote never calls insert or update`() = runTest {
        coEvery { repo.deleteNote(note) } just Runs

        useCase.invoke(note)
        coVerify(exactly = 0) { repo.insertNote(note) }
        coVerify(exactly = 0) { repo.updateNote(note) }

    }
}