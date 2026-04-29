package com.example.noteappcompose.usecase

import app.cash.turbine.test
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.SearchNotesUseCase
import io.mockk.mockk
import com.example.noteappcompose.domain.model.Note
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchNotesUseCaseTest {

    private val repo: NoteRepository = mockk()
    private val useCase = SearchNotesUseCase(repo)
    private val query = "kotlin"
    private fun note(id: Int, title: String) = Note(
        id = id,
        title = title,
        content = "",
        isPinned = false
    )

    @Test
    fun `returns notes that match query`() = runTest {
        val results = listOf(note(1, "Kotlin Flow"), note(2, "kotlin Coroutines"))
        every { repo.searchNotes(query) } returns flowOf(results)
        useCase(query).test {
            val items = awaitItem()
            assert(items.size == 2)
            assert(items.first().title == "Kotlin Flow")
            awaitComplete()
        }

    }

    @Test
    fun `returns empty list when no notes match`() = runTest {
        every { repo.searchNotes(query) } returns flowOf(emptyList())
        useCase(query).test {
            val items = awaitItem()
            assert(items.isEmpty())
            // THEN 2 — verify use case passed "xyz" to repository unchanged
            coVerify  (exactly = 1) { repo.searchNotes(query) }

            // THEN 3 — use case didn't call wrong method
            coVerify(exactly = 0) { repo.getAllNotes() }

            awaitComplete()
        }


    }

}