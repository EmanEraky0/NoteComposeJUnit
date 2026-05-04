package com.example.noteappcompose.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.presentation.ui.NoteEditScreen
import com.example.noteappcompose.presentation.viewmodel.states.NoteUiState
import com.example.noteappcompose.utils.FakeNoteViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

class NoteEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val note= Note(id = 1, title = "edit Note", content = "Content", isPinned = false)

    @Test
    fun saveButtonDisplayed(){

        composeTestRule.setContent {
            NoteEditScreen(
                note = note,
                viewModel = FakeNoteViewModel(initialNotes = emptyList()),
                onBack = {},
            )
        }
        composeTestRule.onNodeWithContentDescription("Save").assertIsDisplayed()
    }

    @Test
    fun filedItemDisplayed(){
        composeTestRule.setContent {
            NoteEditScreen(
                note = note,
                viewModel = FakeNoteViewModel(initialNotes = emptyList()),
                onBack = {},
            )
        }
        composeTestRule.onNodeWithText("edit Note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Content").assertIsDisplayed()
    }

    @Test
    fun typingTitleAndSavingCallsSaveNote() {
        var savedNote: Note? = null

        composeTestRule.setContent {
            NoteEditScreen(note = note,
                viewModel= FakeNoteViewModel(
                onSave = { savedNote = it }  // ← captures saved note
            ), onBack = {})
        }
        composeTestRule
            .onNodeWithText("edit Note")
            .performTextReplacement("My New Note")

        composeTestRule
            .onNodeWithContentDescription("Save")
            .performClick()

        assertNotNull(savedNote)
        assertEquals("My New Note", savedNote?.title)
    }

    @Test
    fun errorMsgTitleAppearWhenIsEmpty(){
        composeTestRule.setContent {
            NoteEditScreen(
                note = note,
                viewModel = FakeNoteViewModel(
                    initialNotes = listOf(Note(id = 1, title = "", content = "Content", isPinned = false)),
                    initialUiState = NoteUiState.Error("Title cannot be empty")
                ),
                onBack = {},
            )
        }
        composeTestRule.onNodeWithText("Title cannot be empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete note").assertIsDisplayed()

    }

}