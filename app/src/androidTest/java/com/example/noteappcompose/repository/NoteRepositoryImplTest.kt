package com.example.noteappcompose.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.noteappcompose.data.local.NoteDatabase
import com.example.noteappcompose.data.local.dao.NoteDao
import com.example.noteappcompose.data.repositoryimpl.NoteRepositoryImpl
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.noteappcompose.domain.model.Note
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NoteRepositoryImplTest {

    private lateinit var database: NoteDatabase
    private lateinit var dao: NoteDao
    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.noteDao()
        repository = NoteRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveNote() = runTest {
        val note = Note(title = "Room test", content = "Content")
        repository.insertNote(note)
        repository.getAllNotes().test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals(note.title, notes[0].title)
            cancel()
        }
    }

    @Test
    fun updateNote() = runTest {
        val note = Note(id = 2, title = "old", content = "Content")
        repository.insertNote(note)
        repository.updateNote(Note(id = note.id, title = "new", content = "Content"))
        repository.getAllNotes().test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("new", notes[0].title)
            cancel()
        }
    }

    @Test
    fun deleteNote() = runTest {
        val note = Note(id = 2, title = "old", content = "Content")
        val id = repository.insertNote(note)
        repository.getNoteById(id.toInt())
        repository.deleteNote(note)
        repository.getAllNotes().test {
            val notes = awaitItem()
            assertTrue(notes.isEmpty())
            cancel()
        }
    }

    @Test
    fun searchReturnsMatchingNotesOnly() = runTest {
        repository.insertNote(Note(id = 1, title = "old", content = "Content"))
        repository.insertNote(Note(id = 2, title = "new", content = "new Content2"))
        repository.searchNotes("new").test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("new", notes.first().title)
            cancel()
        }
    }

    @Test
    fun searchIsCaseInsensitive() = runTest {
        repository.insertNote(Note(id = 1, title = "old", content = "Content"))
        repository.searchNotes("old").test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            cancel()
        }
    }
}