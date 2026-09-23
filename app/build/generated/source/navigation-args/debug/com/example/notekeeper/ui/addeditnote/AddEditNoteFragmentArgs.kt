package com.example.notekeeper.ui.addeditnote

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic

public data class AddEditNoteFragmentArgs(
  public val noteId: Int = -1,
  public val title: String = "New Note",
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("noteId", this.noteId)
    result.putString("title", this.title)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("noteId", this.noteId)
    result.set("title", this.title)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): AddEditNoteFragmentArgs {
      bundle.setClassLoader(AddEditNoteFragmentArgs::class.java.classLoader)
      val __noteId : Int
      if (bundle.containsKey("noteId")) {
        __noteId = bundle.getInt("noteId")
      } else {
        __noteId = -1
      }
      val __title : String?
      if (bundle.containsKey("title")) {
        __title = bundle.getString("title")
        if (__title == null) {
          throw IllegalArgumentException("Argument \"title\" is marked as non-null but was passed a null value.")
        }
      } else {
        __title = "New Note"
      }
      return AddEditNoteFragmentArgs(__noteId, __title)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): AddEditNoteFragmentArgs {
      val __noteId : Int?
      if (savedStateHandle.contains("noteId")) {
        __noteId = savedStateHandle["noteId"]
        if (__noteId == null) {
          throw IllegalArgumentException("Argument \"noteId\" of type integer does not support null values")
        }
      } else {
        __noteId = -1
      }
      val __title : String?
      if (savedStateHandle.contains("title")) {
        __title = savedStateHandle["title"]
        if (__title == null) {
          throw IllegalArgumentException("Argument \"title\" is marked as non-null but was passed a null value")
        }
      } else {
        __title = "New Note"
      }
      return AddEditNoteFragmentArgs(__noteId, __title)
    }
  }
}
