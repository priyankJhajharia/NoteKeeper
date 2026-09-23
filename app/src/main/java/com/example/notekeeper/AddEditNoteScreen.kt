package com.example.notekeeper

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notekeeper.data.local.Note
import com.example.notekeeper.ui.notes.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: NoteViewModel,
    noteId: Int,
    onNoteSaved: () -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val isEditing = noteId != -1

    // Load existing note if editing
    LaunchedEffect(noteId) {
        if (isEditing) {
            val existingNote = viewModel.getNoteById(noteId)
            if (existingNote != null) {
                title = existingNote.title
                content = existingNote.content
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Note" else "New Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        if (isEditing) {
                            viewModel.updateNote(Note(id = noteId, title = title, content = content))
                        } else {
                            viewModel.insertNote(Note(title = title, content = content))
                        }
                        onNoteSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}