package com.tasknote.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tasknote.data.local.entity.CategoryEntity
import com.tasknote.data.local.entity.NoteEntity
import com.tasknote.data.repository.CategoryRepository
import com.tasknote.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val noteRepository: NoteRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _note = MutableStateFlow<NoteEntity?>(null)
    val note: StateFlow<NoteEntity?> = _note.asStateFlow()

    val categories: StateFlow<List<CategoryEntity>> = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            _note.value = noteRepository.getNoteById(noteId)
        }
    }

    fun deleteNote(noteId: Int, onDeleted: () -> Unit) {
        viewModelScope.launch {
            noteRepository.softDelete(noteId)
            onDeleted()
        }
    }

    fun getCategoryName(categoryId: Int): String {
        val category = categories.value.find { it.id == categoryId }
        return category?.name ?: "..."
    }

    fun getCategoryIndex(categoryId: Int): Int {
        return categories.value.indexOfFirst { it.id == categoryId }.coerceAtLeast(0)
    }

    class Factory(
        private val noteRepository: NoteRepository,
        private val categoryRepository: CategoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(noteRepository, categoryRepository) as T
        }
    }
}
