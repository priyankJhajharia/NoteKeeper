package com.example.notekeeper

import android.app.Application
import com.example.notekeeper.data.local.NoteDatabase
import com.example.notekeeper.data.repository.NoteRepository

class NoteKeeperApplication : Application() {
    private val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}
