package com.asfour.data.categories.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Categories
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.persistence.entities.AuditEntity
import com.asfour.data.persistence.entities.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single


class CategoriesRemoteDataSource(private val api: QuizzicalApi) {
    fun loadCategories(): Single<Categories> = api.categories.toSingle()
}

class CategoriesLocalDataSource(private val categoryDao: CategoryDao,
                                private val auditDao: AuditDao) {

    companion object {
        const val CATEGORIES_EXPIRY_SECONDS: Long = 24 * 60 * 60 // One day
    }

    fun fetchCategories(ignoreExpiry: Boolean): Single<Categories> =
            auditDao.isEntityExpired(CategoryEntity.TABLE_NAME, CATEGORIES_EXPIRY_SECONDS)
                    .onErrorReturnItem(false)
                    .filter { expired -> ignoreExpiry || !expired }
                    .toSingle()
                    .flatMap { categoryDao.list() }
                    .filter { !it.isEmpty() }
                    .map { it.map { it.toCategory() }.toList() }
                    .map { Categories(it) }
                    .toSingle()


    fun saveCategories(categories: Categories) = Completable.fromCallable {
        categoryDao.insert(categories.map { CategoryEntity(it) }.toList())
        auditDao.auditEntity(AuditEntity(CategoryEntity.TABLE_NAME))
    }

}

class CategoriesRepository(private val remoteDataSource: CategoriesRemoteDataSource,
                           private val localDataSource: CategoriesLocalDataSource) {

    private var categories: Categories = Categories()

    fun categories(forceRefresh: Boolean = false,
                   ignoreExpiry: Boolean = false): Single<Categories> {

        if (!categories.isEmpty) {
            return Single.just(categories)
        }

        val refresh = remoteDataSource.loadCategories().flatMap {
            categories = it
            localDataSource.saveCategories(it).andThen(Single.just(it))
        }

        if (forceRefresh){
            return refresh
        }

        return localDataSource.fetchCategories(ignoreExpiry)
                .doOnSuccess { categories = it }
                .onErrorResumeNext(refresh)
    }
}