package com.tasknote.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tasknote.data.local.entity.CategoryEntity
import com.tasknote.data.local.entity.NoteEntity
import com.tasknote.data.repository.CategoryRepository
import com.tasknote.data.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val notes: List<NoteEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList()
)

class HomeViewModel(
    private val noteRepository: NoteRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Ubah jadi Set<Int> untuk multi-select
    private val _selectedNoteIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedNoteIds: StateFlow<Set<Int>> = _selectedNoteIds.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val notesFlow = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) noteRepository.getAllNotes()
            else noteRepository.searchNotes(query)
        }

    val uiState: StateFlow<HomeUiState> = combine(
        notesFlow,
        categoryRepository.getAllCategories()
    ) { notes, categories ->
        HomeUiState(notes = notes, categories = categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleNoteSelection(noteId: Int) {
        val current = _selectedNoteIds.value.toMutableSet()
        if (current.contains(noteId)) {
            current.remove(noteId)
        } else {
            current.add(noteId)
        }
        _selectedNoteIds.value = current
    }

    fun clearSelection() {
        _selectedNoteIds.value = emptySet()
    }

    fun deleteSelectedNotes(onDeleted: () -> Unit = {}) {
        val ids = _selectedNoteIds.value
        if (ids.isEmpty()) return
        
        viewModelScope.launch {
            ids.forEach { id ->
                noteRepository.softDelete(id)
            }
            _selectedNoteIds.value = emptySet()
            onDeleted()
        }
    }

    fun getCategoryName(categoryId: Int, categories: List<CategoryEntity>): String {
        return categories.find { it.id == categoryId }?.name ?: "Umum"
    }

    fun getCategoryIndex(categoryId: Int, categories: List<CategoryEntity>): Int {
        return categories.indexOfFirst { it.id == categoryId }.coerceAtLeast(0)
    }

    class Factory(
        private val noteRepository: NoteRepository,
        private val categoryRepository: CategoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(noteRepository, categoryRepository) as T
        }
    }
}
