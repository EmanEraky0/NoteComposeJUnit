package com.example.noteappcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteappcompose.data.local.dao.NoteDao
import com.example.noteappcompose.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}