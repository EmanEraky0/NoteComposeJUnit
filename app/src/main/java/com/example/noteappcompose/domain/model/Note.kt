package com.example.noteappcompose.domain.model

data class Note (
    val id: Int = 0,
    val title: String="",
    val content: String="",
    val createdAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)
