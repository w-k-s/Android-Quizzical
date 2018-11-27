package com.asfour.data.categories.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Categories
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.persistence.entities.AuditEntity
import com.asfour.data.persistence.entities.CategoryEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit


class CategoriesRemoteDataSource(private val api: QuizzicalApi) {
    suspend fun loadCategories(): Categories = api.categories.await()
}

class CategoriesLocalDataSource(private val categoryDao: CategoryDao,
                                private val auditDao: AuditDao) {

    suspend fun fetchCategories(ignoreExpiry: Boolean): Categories {
        return GlobalScope.async {

            val expired = auditDao.isEntityExpired(CategoryEntity.TABLE_NAME, TimeUnit.DAYS.toSeconds(7))

            if (!ignoreExpiry && expired) {
                Categories()
            } else {
                Categories(categoryDao.list().map { it.toCategory() })
            }

        }.await()
    }

    suspend fun saveCategories(categories: Categories) = GlobalScope.async {
        categoryDao.deleteAll()
        categoryDao.insert(categories.map { CategoryEntity(it) }.toList())
        auditDao.auditEntity(AuditEntity(CategoryEntity.TABLE_NAME))
    }.await()

}

class CategoriesRepository(private val remoteDataSource: CategoriesRemoteDataSource,
                           private val localDataSource: CategoriesLocalDataSource) {

    private var categories: Categories = Categories()

    suspend fun categories(forceRefresh: Boolean = false,
                           ignoreExpiry: Boolean = false): Categories {

        if (!categories.isEmpty) {
            return categories
        }

        if (!forceRefresh) {
            val categories = localDataSource.fetchCategories(ignoreExpiry)
            if (!categories.isEmpty) {
                this.categories = categories
                return categories
            }
        }

        val categories = remoteDataSource.loadCategories()
        if (!categories.isEmpty) {
            this.categories = categories
            localDataSource.saveCategories(categories)
        }

        return categories
    }
}