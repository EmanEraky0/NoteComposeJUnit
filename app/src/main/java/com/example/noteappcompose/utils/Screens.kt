package com.example.noteappcompose.utils

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteappcompose.domain.model.Note
import com.example.noteappcompose.presentation.ui.NoteEditScreen
import com.example.noteappcompose.presentation.ui.NoteListScreen
import com.google.gson.Gson

sealed class Screen(val route: String) {
    data object NoteList : Screen("note_list")
    object EditNote : Screen("edit_note/{note}") {
        fun createRoute(note: Note? = null): String {
            val json = Uri.encode(Gson().toJson(note ?: Note(id = 0, title = "", content = "")))
            return "edit_note/$json"
        }
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        composable(Screen.NoteList.route) {
            NoteListScreen(
                onNoteClick = { note ->
                    navController.navigate(Screen.EditNote.createRoute(note))
                },
                onAddClick = {
                    navController.navigate(Screen.EditNote.createRoute(null))

                }
            )
        }
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(navArgument("note") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("note")
            val note = Gson().fromJson(json, Note::class.java)
            NoteEditScreen(
                note = note,
                onBack = { navController.popBackStack() }
            )
        }

    }
}