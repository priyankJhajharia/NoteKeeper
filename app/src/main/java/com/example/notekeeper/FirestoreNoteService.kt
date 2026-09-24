package com.example.notekeeper

import com.example.notekeeper.data.local.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreNoteService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun userNotesCollection() =
        auth.currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).collection("notes")
        }

    suspend fun uploadNote(note: Note) {
        userNotesCollection()?.document(note.id.toString())?.set(note)?.await()
    }

    suspend fun deleteNote(noteId: Int) {
        userNotesCollection()?.document(noteId.toString())?.delete()?.await()
    }

    suspend fun getAllNotes(): List<Note> {
        val snapshot = userNotesCollection()?.get()?.await()
        return snapshot?.toObjects(Note::class.java) ?: emptyList()
    }
}