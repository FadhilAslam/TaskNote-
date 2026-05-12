package com.tasknote

import android.app.Application
import com.tasknote.data.datastore.ThemePreferences
import com.tasknote.data.local.TaskNoteDatabase
import com.tasknote.data.repository.CategoryRepository
import com.tasknote.data.repository.NoteRepository

class TaskNoteApplication : Application() {

    private val database by lazy { TaskNoteDatabase.getDatabase(this) }

    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val themePreferences by lazy { ThemePreferences(this) }
}
