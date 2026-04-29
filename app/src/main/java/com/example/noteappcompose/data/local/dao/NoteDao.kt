package com.example.noteappcompose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteappcompose.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // Room automatically re-emits whenever the DB changes
    // returns Flow a stream of values over time — no suspend needed
    // So when you insert/update/delete a note...
    // ← triggers Room to emit new list
    // ← triggers composables to recompose

    //  So insertNote, updateNote, deleteNote and getNoteById all need suspend because they do one thing and finish.
    //  But getAllNotes and searchNotes stay open forever emitting updates, so they just return Flow.

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}