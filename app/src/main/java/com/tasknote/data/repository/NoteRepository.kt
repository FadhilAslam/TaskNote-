package com.tasknote.data.repository

import com.tasknote.data.local.dao.NoteDao
import com.tasknote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun getDeletedNotes(): Flow<List<NoteEntity>> = noteDao.getDeletedNotes()

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)

    suspend fun getNoteById(id: Int): NoteEntity? = noteDao.getNoteById(id)

    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun softDelete(id: Int) = noteDao.softDelete(id)

    suspend fun restoreNote(id: Int) = noteDao.restoreNote(id)

    suspend fun hardDelete(note: NoteEntity) = noteDao.hardDelete(note)

    suspend fun emptyRecycleBin() = noteDao.emptyRecycleBin()
}
