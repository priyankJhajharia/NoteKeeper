package com.example.notekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notekeeper.ui.notes.NoteViewModel
import com.example.notekeeper.ui.notes.NoteViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class NoteListComposeActivity : ComponentActivity() {

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteKeeperApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        if (isLoggedIn) {
            viewModel.syncFromCloud()
        }

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val startDestination = if (isLoggedIn) "noteList" else "login"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                viewModel.syncFromCloud()
                                navController.navigate("noteList") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("noteList") {
                        NoteListScreen(
                            viewModel = viewModel,
                            onAddNoteClick = { navController.navigate("addEditNote/-1") },
                            onNoteClick = { noteId -> navController.navigate("addEditNote/$noteId") },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("noteList") { inclusive = true }
                                }
                            }
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