package com.example.notekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.notekeeper.ui.notes.NoteViewModel
import com.example.notekeeper.ui.notes.NoteViewModelFactory

class NoteListComposeActivity : ComponentActivity() {

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteKeeperApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "noteList") {
                    composable("noteList") {
                        NoteListScreen(
                            viewModel = viewModel,
                            onAddNoteClick = { navController.navigate("addEditNote/-1") },
                            onNoteClick = { noteId -> navController.navigate("addEditNote/$noteId") }
                        )
                    }
                    composable(
                        route = "addEditNote/{noteId}",
                        arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
                        AddEditNoteScreen(
                            viewModel = viewModel,
                            noteId = noteId,
                            onNoteSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}