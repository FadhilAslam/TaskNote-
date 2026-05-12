package com.tasknote.ui.screen.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.tasknote.TaskNoteApplication
import com.tasknote.ui.theme.CategoryColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditScreen(
    noteId: Int?,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskNoteApplication
    val viewModel: AddEditViewModel = viewModel(
        factory = AddEditViewModel.Factory(app.noteRepository, app.categoryRepository)
    )

    LaunchedEffect(noteId) {
        noteId?.let { viewModel.loadNote(it) }
    }

    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    val isEditing = noteId != null
    val contentFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                                if (isEditing) "Edit Catatan" else "Catatan Baru",
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
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = androidx.compose.ui.graphics.Color.Transparent,
                            titleContentColor = androidx.compose.ui.graphics.Color.White,
                            navigationIconContentColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Title field
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Judul") },
                placeholder = { Text("Masukkan judul catatan...") },
                isError = uiState.titleError != null,
                supportingText = {
                    uiState.titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { contentFocusRequester.requestFocus() }
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content field
            OutlinedTextField(
                value = uiState.content,
                onValueChange = viewModel::onContentChange,
                label = { Text("Isi Catatan") },
                placeholder = { Text("Tulis catatan di sini...") },
                isError = uiState.contentError != null,
                supportingText = {
                    uiState.contentError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                minLines = 6,
                maxLines = 15,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(contentFocusRequester),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category selector
            Text(
                text = "Kategori",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    val chipColor = CategoryColors.getOrElse(index % CategoryColors.size) {
                        CategoryColors[0]
                    }
                    val isSelected = uiState.selectedCategoryId == category.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onCategoryChange(category.id) },
                        label = { Text(category.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor.copy(alpha = 0.2f),
                            selectedLabelColor = chipColor
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            selectedBorderColor = chipColor,
                            borderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = viewModel::saveNote,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isEditing) "Simpan Perubahan" else "Simpan Catatan",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}