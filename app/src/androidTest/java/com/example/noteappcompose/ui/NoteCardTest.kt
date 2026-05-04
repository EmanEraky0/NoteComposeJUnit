package com.example.noteappcompose.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.presentation.component.NoteCard
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class NoteCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val note = Note(id = 1, title = "Card Title", content = "Card Content", isPinned = false)

    @Test
    fun titleContentAppear() {
        composeTestRule.setContent {
            NoteCard(
                note = note,
                onClick = {},
                onDelete = {}
            )
        }
        composeTestRule.onNodeWithText("Card Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Card Content").assertIsDisplayed()
    }

    @Test
    fun deleteBtnAndDialog(){
        composeTestRule.setContent {
            NoteCard(
                note = note,
                onClick = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Delete note").performClick()
        composeTestRule.onNodeWithText("Delete note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Are you sure you want to delete \"Card Title\"?").assertIsDisplayed()
    }

    @Test
    fun deleteDialogConfirm(){
        var deleted = false
        composeTestRule.setContent {
            NoteCard(
                note = note,
                onClick = {},
                onDelete = {deleted=true}
            )
        }
        composeTestRule.onNodeWithContentDescription("Delete note").performClick()
        composeTestRule.onNodeWithText("Delete note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").performClick()
        assertTrue(deleted)

    }

    @Test
    fun cancelDialogStillAppearNote(){
        composeTestRule.setContent {
            NoteCard(
                note = note,
                onClick = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Delete note").performClick()
        composeTestRule.onNodeWithText("Delete note").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("Delete note").assertDoesNotExist()
        composeTestRule.onNodeWithText("Card Title").assertIsDisplayed()

    }



}