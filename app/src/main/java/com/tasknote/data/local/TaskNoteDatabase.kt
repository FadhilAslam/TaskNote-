package com.tasknote.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tasknote.data.local.dao.CategoryDao
import com.tasknote.data.local.dao.NoteDao
import com.tasknote.data.local.entity.CategoryEntity
import com.tasknote.data.local.entity.NoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [NoteEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskNoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: TaskNoteDatabase? = null

        fun getDatabase(context: Context): TaskNoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskNoteDatabase::class.java,
                    "tasknote_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Gunakan db langsung, bukan INSTANCE (yang bisa null saat onCreate)
                            CoroutineScope(Dispatchers.IO).launch {
                                db.execSQL("INSERT INTO categories (id, name) VALUES (1, 'Umum')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (2, 'Pekerjaan')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (3, 'Pribadi')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (4, 'Ide')")
                                db.execSQL("INSERT INTO categories (id, name) VALUES (5, 'Penting')")
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
