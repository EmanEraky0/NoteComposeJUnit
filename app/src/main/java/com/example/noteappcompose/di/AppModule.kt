package com.example.noteappcompose.di

import androidx.room.Room
import com.example.noteappcompose.data.local.NoteDatabase
import com.example.noteappcompose.data.repositoryimpl.NoteRepositoryImpl
import com.example.noteappcompose.domain.repository.NoteRepository
import com.example.noteappcompose.domain.usecase.DeleteNoteUseCase
import com.example.noteappcompose.domain.usecase.GetNotesUseCase
import com.example.noteappcompose.domain.usecase.SaveNoteUseCase
import com.example.noteappcompose.domain.usecase.SearchNotesUseCase
import com.example.noteappcompose.presentation.viewmodel.NoteViewModel
import com.example.noteappcompose.utils.NoteValidator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// di/AppModule.kt
val appModule = module {

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

    // DAO
    single { get<NoteDatabase>().noteDao() }

    // Validator
    single { NoteValidator() }

    // Repository
    single<NoteRepository> { NoteRepositoryImpl(get()) }

    // Use Cases
    factory { GetNotesUseCase(get()) }
    factory { SearchNotesUseCase(get()) }
    factory { SaveNoteUseCase(get(), get()) }
    factory { DeleteNoteUseCase(get()) }


    // ViewModel
    viewModel {
        NoteViewModel(get(), get(), get(), get())
    }
}