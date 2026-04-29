package com.example.noteappcompose.viewmodel

import app.cash.turbine.Event
import app.cash.turbine.test
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.DeleteNoteUseCase
import com.example.noteappcompose.domain.usecase.GetNotesUseCase
import com.example.noteappcompose.domain.usecase.SaveNoteUseCase
import com.example.noteappcompose.domain.usecase.SearchNotesUseCase
import com.example.noteappcompose.presentation.viewmodel.NoteViewModel
import com.example.noteappcompose.utils.NoteValidator
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.presentation.viewmodel.states.NoteUiState
import io.mockk.coEvery
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private val getNotesUseCase: GetNotesUseCase = mockk()
    private val searchNotesUseCase: SearchNotesUseCase = mockk()
    private val saveNoteUseCase: SaveNoteUseCase = mockk()
    private val deleteNoteUseCase: DeleteNoteUseCase = mockk()
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NoteViewModel(
            getNotesUseCase = getNotesUseCase,
            searchNotesUseCase = searchNotesUseCase,
            saveNoteUseCase = saveNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase
        )
    }
    @Test
    fun `initial search query is empty`() = runTest{
        viewModel.searchQuery.test {
            assertEquals("", awaitItem())
        }
    }


    @Test
    fun `saveNote emits Saved state on success`() = runTest {
        val note = Note(id = 0, title = "Test", content = "Body")
        coEvery { saveNoteUseCase(note) } returns Result.success(Unit)

        viewModel.uiState.test {
            viewModel.saveNote(note)
            awaitItem()
            //option 1 the Best
            val states = cancelAndConsumeRemainingEvents()
                .filterIsInstance<Event.Item<NoteUiState>>()
                .map { it.value }
            assertTrue(states.any { it is NoteUiState.Saved })
            //option2
//            assertEquals(NoteUiState.Loading,awaitItem())
//            assertEquals(NoteUiState.Saved,awaitItem())
        }

    }
    @Test
    fun `saveNote emits Error state on failure`() = runTest {
        val note = Note(id = 0, title = "", content = "")
        coEvery { saveNoteUseCase(note) } returns
                Result.failure(Exception("Title cannot be empty"))

        viewModel.uiState.test {
            viewModel.saveNote(note)
            val states = cancelAndConsumeRemainingEvents()
                .filterIsInstance<Event.Item<NoteUiState>>()
                .map { it.value }
            val error = states.filterIsInstance<NoteUiState.Error>().firstOrNull()
            assertNotNull(error)
            assertEquals("Title cannot be empty", error?.message)
        }

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

}