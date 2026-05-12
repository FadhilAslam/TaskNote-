package com.tasknote.ui.screen.recyclebin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tasknote.data.local.entity.CategoryEntity
import com.tasknote.data.local.entity.NoteEntity
import com.tasknote.data.repository.CategoryRepository
import com.tasknote.data.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecycleBinViewModel(
    private val noteRepository: NoteRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    val deletedNotes: StateFlow<List<NoteEntity>> = noteRepository.getDeletedNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<CategoryEntity>> = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun restoreNote(noteId: Int) {
        viewModelScope.launch {
            noteRepository.restoreNote(noteId)
        }
    }

    fun hardDeleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.hardDelete(note)
        }
    }

    fun emptyRecycleBin() {
        viewModelScope.launch {
            noteRepository.emptyRecycleBin()
        }
    }

    fun getCategoryName(categoryId: Int): String {
        return categories.value.find { it.id == categoryId }?.name ?: "Umum"
    }

    class Factory(
        private val noteRepository: NoteRepository,
        private val categoryRepository: CategoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecycleBinViewModel(noteRepository, categoryRepository) as T
        }
    }
}
