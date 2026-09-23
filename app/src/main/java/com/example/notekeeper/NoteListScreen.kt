package com.example.notekeeper

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notekeeper.data.local.Note
import com.example.notekeeper.ui.notes.NoteViewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Int) -> Unit
) {
    val notes by viewModel.notes.observeAsState(emptyList())
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Notes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onClick = { onNoteClick(note.id) },
                    onLongClick = { noteToDelete = note }
                )
            }
        }
    }

    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNoteById(note.id)
                    noteToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
        }
    }
}