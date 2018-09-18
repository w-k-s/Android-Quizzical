package com.asfour.data.categories.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Categories
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.persistence.entities.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single


class CategoriesRemoteDataSource(private val api: QuizzicalApi) {
    fun loadCategories(): Single<Categories> = api.categories.toSingle()
}

class CategoriesLocalDataSource(private val categoryDao: CategoryDao) {

    fun fetchCategories(): Single<Categories> = categoryDao.list()
            .filter { !it.isEmpty() }
            .map { it.map { it.toCategory() }.toList() }
            .map { Categories(it) }
            .toSingle()

    fun saveCategories(categories: Categories) = Completable.fromCallable {
        categoryDao.insert(categories.map { CategoryEntity(it) }.toList())
    }

}

class CategoriesRepository(private val remoteDataSource: CategoriesRemoteDataSource,
                           private val localDataSource: CategoriesLocalDataSource) {

    fun categories(): Single<Categories> {
        return localDataSource.fetchCategories()
                .onErrorResumeNext(
                        remoteDataSource.loadCategories().flatMap {
                            localDataSource.saveCategories(it).andThen(Single.just(it))
                        })
    }
}