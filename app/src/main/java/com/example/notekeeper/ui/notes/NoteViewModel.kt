package com.example.notekeeper.ui.notes

import androidx.lifecycle.*
import com.example.notekeeper.FirestoreNoteService
import com.example.notekeeper.data.local.Note
import com.example.notekeeper.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val firestoreService = FirestoreNoteService()

    private val _searchQuery = MutableLiveData("")

    val notes: LiveData<List<Note>> = _searchQuery.switchMap { query ->
        if (query.isNullOrBlank()) repository.allNotes.asLiveData()
        else repository.searchNotes(query).asLiveData()
    }

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun insertNote(note: Note) = viewModelScope.launch {
        val generatedId = repository.insertNote(note)
        firestoreService.uploadNote(note.copy(id = generatedId.toInt()))
        _navigateBack.value = true
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
        firestoreService.uploadNote(note)
        _navigateBack.value = true
    }

    fun deleteNoteById(id: Int) = viewModelScope.launch {
        repository.deleteNoteById(id)
        firestoreService.deleteNote(id)
    }

    fun clearLocalNotes() = viewModelScope.launch {
        repository.deleteAllNotes()
    }

    suspend fun getNoteById(id: Int): Note? = repository.getNoteById(id)

    fun syncFromCloud() = viewModelScope.launch {
        val cloudNotes = firestoreService.getAllNotes()
        cloudNotes.forEach { note ->
            repository.insertNote(note)
        }
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}