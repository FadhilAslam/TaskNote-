package com.tasknote.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tasknote.TaskNoteApplication
import com.tasknote.ui.components.ConfirmDialog
import com.tasknote.ui.components.NoteCard
import com.tasknote.ui.components.NoteSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddNote: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onRecycleBinClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onEditNote: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskNoteApplication
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(app.noteRepository, app.categoryRepository)
    )

    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedNoteIds by viewModel.selectedNoteIds.collectAsState()

    val isSelectionMode = selectedNoteIds.isNotEmpty()
    val selectedCount = selectedNoteIds.size
    val isSingleSelection = selectedCount == 1

    var showDeleteDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = isSelectionMode) {
        viewModel.clearSelection()
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title = "Hapus $selectedCount Catatan",
            message = if (selectedCount == 1)
                "Catatan akan dipindahkan ke Recycle Bin. Lanjutkan?"
            else
                "$selectedCount catatan akan dipindahkan ke Recycle Bin. Lanjutkan?",
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteSelectedNotes()
            },
            onDismiss = {
                showDeleteDialog = false
                viewModel.clearSelection()
            }
        )
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isSelectionMode,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(androidx.compose.ui.graphics.Color.Black)
                ) {
                    Spacer(modifier = Modifier.height(
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp
                    ))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                        color = androidx.compose.ui.graphics.Color(0xFF4F6AF5),
                        shadowElevation = 4.dp
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "$selectedCount dipilih",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { viewModel.clearSelection() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Batal")
                                }
                            },
                            actions = {
                                // Edit hanya muncul kalau pilih 1 item
                                if (isSingleSelection) {
                                    IconButton(onClick = {
                                        val noteId = selectedNoteIds.first()
                                        viewModel.clearSelection()
                                        onEditNote(noteId)
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                }
                                // Delete selalu muncul
                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                titleContentColor = androidx.compose.ui.graphics.Color.White,
                                navigationIconContentColor = androidx.compose.ui.graphics.Color.White,
                                actionIconContentColor = androidx.compose.ui.graphics.Color.White
                            )
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = !isSelectionMode,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(androidx.compose.ui.graphics.Color.Black)
                ) {
                    Spacer(modifier = Modifier.height(
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp
                    ))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                        color = androidx.compose.ui.graphics.Color(0xFF4F6AF5),
                        shadowElevation = 4.dp
                    ) {
                        TopAppBar(
                            title = {
                                Text("TaskNote", style = MaterialTheme.typography.titleLarge)
                            },
                            actions = {
                                IconButton(onClick = onRecycleBinClick) {
                                    Icon(Icons.Default.Delete, contentDescription = "Recycle Bin")
                                }
                                IconButton(onClick = onSettingsClick) {
                                    Icon(Icons.Default.Settings, contentDescription = "Pengaturan")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                titleContentColor = androidx.compose.ui.graphics.Color.White,
                                navigationIconContentColor = androidx.compose.ui.graphics.Color.White,
                                actionIconContentColor = androidx.compose.ui.graphics.Color.White
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = onAddNote,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            NoteSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (searchQuery.isBlank()) "Belum ada catatan"
                            else "Catatan tidak ditemukan",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isBlank()) "Tekan tombol + untuk menambah catatan baru"
                            else "Coba kata kunci lain",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(uiState.notes, key = { it.id }) { note ->
                        val categoryName = viewModel.getCategoryName(note.categoryId, uiState.categories)
                        val categoryIndex = viewModel.getCategoryIndex(note.categoryId, uiState.categories)
                        val isSelected = selectedNoteIds.contains(note.id)

                        NoteCard(
                            note = note,
                            categoryName = categoryName,
                            categoryIndex = categoryIndex,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelectionMode) {
                                    // Saat selection mode, tap toggle pilihan
                                    viewModel.toggleNoteSelection(note.id)
                                } else {
                                    onNoteClick(note.id)
                                }
                            },
                            onLongClick = {
                                // Long press mulai / tambah selection
                                viewModel.toggleNoteSelection(note.id)
                            }
                        )
                    }
                }
            }
        }
    }
}