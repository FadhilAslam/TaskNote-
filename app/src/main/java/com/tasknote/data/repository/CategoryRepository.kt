package com.tasknote.data.repository

import com.tasknote.data.local.dao.CategoryDao
import com.tasknote.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun getCategoryById(id: Int): CategoryEntity? = categoryDao.getCategoryById(id)

    suspend fun insertCategory(category: CategoryEntity) = categoryDao.insertCategory(category)
}
