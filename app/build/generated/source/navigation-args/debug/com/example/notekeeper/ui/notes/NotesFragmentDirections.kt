package com.example.notekeeper.ui.notes

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.notekeeper.R
import kotlin.Int
import kotlin.String

public class NotesFragmentDirections private constructor() {
  private data class ActionNotesFragmentToAddEditNoteFragment(
    public val noteId: Int = -1,
    public val title: String = "New Note",
  ) : NavDirections {
    public override val actionId: Int = R.id.action_notesFragment_to_addEditNoteFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("noteId", this.noteId)
        result.putString("title", this.title)
        return result
      }
  }

  public companion object {
    public fun actionNotesFragmentToAddEditNoteFragment(noteId: Int = -1, title: String =
        "New Note"): NavDirections = ActionNotesFragmentToAddEditNoteFragment(noteId, title)
  }
}
