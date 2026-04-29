package com.example.noteappcompose.usecase

import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.SaveNoteUseCase
import com.example.noteappcompose.utils.NoteValidator
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveNoteUseCaseTest {
    private val repo: NoteRepository = mockk()
    private val validator = NoteValidator()
    private val useCase = SaveNoteUseCase(repo, validator)

    @Test
    fun `valid new note is inserted`() = runTest {
        val note = Note(id = 0, title = "Normal", content = "testInsert", isPinned = false)

        coEvery { repo.insertNote(note) } returns 1L
        val result = useCase.invoke(note)
        assert(result.isSuccess)
        coVerify(exactly = 1) { repo.insertNote(note) }


    }
    @Test
    fun `valid  note is updated`() = runTest {
        val note = Note(id = 10, title = "Normal", content = "testInsert", isPinned = false)
        coEvery { repo.updateNote(note) } just Runs
        val result = useCase.invoke(note)
        assert(result.isSuccess)

        coVerify(exactly = 1) { repo.updateNote(note) }
        coVerify(exactly = 0) { repo.insertNote(any()) }
    }
}