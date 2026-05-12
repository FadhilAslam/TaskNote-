package com.tasknote.ui.screen.addedit

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

data class AddEditUiState(
    val title: String = "",
    val content: String = "",
    val selectedCategoryId: Int = 1,
    val titleError: String? = null,
    val contentError: String? = null,
    val isSaved: Boolean = false,
)

class AddEditViewModel(
    private val noteRepository: NoteRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    private var existingNoteId: Int? = null

    val categories: StateFlow<List<CategoryEntity>> = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId) ?: return@launch
            existingNoteId = note.id
            _uiState.value = _uiState.value.copy(
                title = note.title,
                content = note.content,
                selectedCategoryId = note.categoryId
            )
        }
    }

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value, titleError = null)
    }

    fun onContentChange(value: String) {
        _uiState.value = _uiState.value.copy(content = value, contentError = null)
    }

    fun onCategoryChange(categoryId: Int) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
    }

    fun saveNote() {
        val state = _uiState.value
        var hasError = false

        if (state.title.isBlank()) {
            _uiState.value = _uiState.value.copy(titleError = "Judul tidak boleh kosong")
            hasError = true
        }
        if (state.content.isBlank()) {
            _uiState.value = _uiState.value.copy(contentError = "Isi catatan tidak boleh kosong")
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val id = existingNoteId

            if (id == null) {
                noteRepository.insertNote(
                    NoteEntity(
                        title = state.title.trim(),
                        content = state.content.trim(),
                        categoryId = state.selectedCategoryId,
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                val existing = noteRepository.getNoteById(id) ?: return@launch
                noteRepository.updateNote(
                    existing.copy(
                        title = state.title.trim(),
                        content = state.content.trim(),
                        categoryId = state.selectedCategoryId,
                        updatedAt = now
                    )
                )
            }
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }

    class Factory(
        private val noteRepository: NoteRepository,
        private val categoryRepository: CategoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddEditViewModel(noteRepository, categoryRepository) as T
        }
    }
}
