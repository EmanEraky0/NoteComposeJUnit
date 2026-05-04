package com.example.noteappcompose.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.noteappcompose.presentation.ui.NoteListScreen
import junit.framework.TestCase.assertTrue
import com.example.noteappcompose.domain.model.Note
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class NoteListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBarIsDisplay() {
        composeTestRule.setContent { 
            NoteListScreen(
                onNoteClick = {},
                onAddClick = {}
            )
        }
        composeTestRule.onNodeWithText("Search notes...").assertIsDisplayed()

    }

    @Test
    fun errorMsgShowWhenNoNotes(){
        composeTestRule.setContent {
            NoteListScreen(
                onNoteClick = {},
                onAddClick = {}
            )
        }
        composeTestRule.onNodeWithText("No notes yet.\nTap + to create one").assertIsDisplayed()

    }
    @Test
    fun addNoteClicked(){
        var clicked = false
        composeTestRule.setContent {
            NoteListScreen(
                onNoteClick = {},
                onAddClick = {
                    clicked=true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add note").performClick()
        assertTrue(clicked)
    }

    @Test
    fun noteListDisplayed(){
        composeTestRule.setContent {
            NoteListScreen(
                onNoteClick = {},
                onAddClick = {}
            )
        }
        composeTestRule.onNodeWithText("Note One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Note Two").assertIsDisplayed()
    }

    @Test
    fun noteClickedWithNote(){
       val note= Note(id = 1, title = "Clickable Note", content = "Content", isPinned = false)
        var clickedNote: Note? = null

        composeTestRule.setContent {
            NoteListScreen(
                onNoteClick = {clickedNote = it},
                onAddClick = {}
            )
        }
        composeTestRule.onNodeWithText("Clickable Note").performClick()
        assertEquals(note , clickedNote)
    }

    @Test
    fun searchUpdateQuery(){
        composeTestRule.setContent {
            NoteListScreen(
                onNoteClick = {},
                onAddClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Search notes...")
            .performTextInput("kotlin")


        composeTestRule.onNodeWithText("kotlin").assertIsDisplayed()
    }
}