package com.tasknote.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.tasknote.`data`.local.entity.NoteEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class NoteDao_Impl(
  __db: RoomDatabase,
) : NoteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNoteEntity: EntityInsertAdapter<NoteEntity>

  private val __deleteAdapterOfNoteEntity: EntityDeleteOrUpdateAdapter<NoteEntity>

  private val __updateAdapterOfNoteEntity: EntityDeleteOrUpdateAdapter<NoteEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfNoteEntity = object : EntityInsertAdapter<NoteEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `notes` (`id`,`title`,`content`,`categoryId`,`createdAt`,`updatedAt`,`isDeleted`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NoteEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.content)
        statement.bindLong(4, entity.categoryId.toLong())
        statement.bindLong(5, entity.createdAt)
        statement.bindLong(6, entity.updatedAt)
        val _tmp: Int = if (entity.isDeleted) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }
    this.__deleteAdapterOfNoteEntity = object : EntityDeleteOrUpdateAdapter<NoteEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `notes` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteEntity) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfNoteEntity = object : EntityDeleteOrUpdateAdapter<NoteEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `notes` SET `id` = ?,`title` = ?,`content` = ?,`categoryId` = ?,`createdAt` = ?,`updatedAt` = ?,`isDeleted` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.content)
        statement.bindLong(4, entity.categoryId.toLong())
        statement.bindLong(5, entity.createdAt)
        statement.bindLong(6, entity.updatedAt)
        val _tmp: Int = if (entity.isDeleted) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertNote(note: NoteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfNoteEntity.insert(_connection, note)
  }

  public override suspend fun hardDelete(note: NoteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfNoteEntity.handle(_connection, note)
  }

  public override suspend fun updateNote(note: NoteEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfNoteEntity.handle(_connection, note)
  }

  public override fun getAllNotes(): Flow<List<NoteEntity>> {
    val _sql: String = "SELECT * FROM notes WHERE isDeleted = 0 ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _result: MutableList<NoteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCategoryId: Int
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpIsDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp != 0
          _item = NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDeletedNotes(): Flow<List<NoteEntity>> {
    val _sql: String = "SELECT * FROM notes WHERE isDeleted = 1 ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _result: MutableList<NoteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCategoryId: Int
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpIsDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp != 0
          _item = NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNoteById(id: Int): NoteEntity? {
    val _sql: String = "SELECT * FROM notes WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _result: NoteEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCategoryId: Int
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpIsDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp != 0
          _result = NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun searchNotes(query: String): Flow<List<NoteEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM notes 
        |        WHERE isDeleted = 0 
        |        AND (title LIKE '%' || ? || '%' OR content LIKE '%' || ? || '%')
        |        ORDER BY updatedAt DESC
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("notes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _result: MutableList<NoteEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCategoryId: Int
          _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpIsDeleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDeleted).toInt()
          _tmpIsDeleted = _tmp != 0
          _item = NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategoryId,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDelete(id: Int, timestamp: Long) {
    val _sql: String = "UPDATE notes SET isDeleted = 1, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreNote(id: Int, timestamp: Long) {
    val _sql: String = "UPDATE notes SET isDeleted = 0, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun emptyRecycleBin() {
    val _sql: String = "DELETE FROM notes WHERE isDeleted = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
