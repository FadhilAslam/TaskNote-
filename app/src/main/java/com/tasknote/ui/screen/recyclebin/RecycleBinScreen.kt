package com.tasknote.ui.screen.recyclebin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tasknote.TaskNoteApplication
import com.tasknote.data.local.entity.NoteEntity
import com.tasknote.ui.components.ConfirmDialog
import com.tasknote.ui.components.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskNoteApplication
    val viewModel: RecycleBinViewModel = viewModel(
        factory = RecycleBinViewModel.Factory(app.noteRepository, app.categoryRepository)
    )

    val deletedNotes by viewModel.deletedNotes.collectAsState()
    var showEmptyBinDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<NoteEntity?>(null) }

    if (showEmptyBinDialog) {
        ConfirmDialog(
            title = "Kosongkan Recycle Bin",
            message = "Semua catatan akan dihapus permanen dan tidak bisa dikembalikan. Lanjutkan?",
            onConfirm = {
                viewModel.emptyRecycleBin()
                showEmptyBinDialog = false
            },
            onDismiss = { showEmptyBinDialog = false },
            confirmText = "Hapus Semua"
        )
    }

    noteToDelete?.let { note ->
        ConfirmDialog(
            title = "Hapus Permanen",
            message = "\"${note.title}\" akan dihapus permanen. Tindakan ini tidak bisa dibatalkan.",
            onConfirm = {
                viewModel.hardDeleteNote(note)
                noteToDelete = null
            },
            onDismiss = { noteToDelete = null },
            confirmText = "Hapus"
        )
    }

    Scaffold(
        topBar = {
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
                                "Recycle Bin",
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali"
                                )
                            }
                        },
                        actions = {
                            if (deletedNotes.isNotEmpty()) {
                                TextButton(
                                    onClick = { showEmptyBinDialog = true }
                                ) {
                                    Text(
                                        "Kosongkan",
                                        color = androidx.compose.ui.graphics.Color.White
                                    )
                                }
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
    ) { innerPadding ->
        if (deletedNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = "Recycle Bin kosong",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Catatan yang dihapus akan muncul di sini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(deletedNotes, key = { it.id }) { note ->
                    RecycleBinNoteCard(
                        note = note,
                        categoryName = viewModel.getCategoryName(note.categoryId),
                        onRestore = { viewModel.restoreNote(note.id) },
                        onDelete = { noteToDelete = note }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecycleBinNoteCard(
    note: NoteEntity,
    categoryName: String,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kategori: $categoryName  •  ${formatDate(note.updatedAt)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRestore,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Text("  Restore")
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text("  Hapus", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}