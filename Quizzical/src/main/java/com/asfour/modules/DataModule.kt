package com.asfour.modules

import com.asfour.data.categories.source.CategoriesLocalDataSource
import com.asfour.data.categories.source.CategoriesRemoteDataSource
import com.asfour.data.categories.source.CategoriesRepository
import com.asfour.data.persistence.db.AppDatabase
import com.asfour.data.questions.source.QuestionsLocalDataSource
import com.asfour.data.questions.source.QuestionsRemoteDataSource
import com.asfour.data.questions.source.QuestionsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


val databaseModule = module {
    single { AppDatabase.getInstance(androidContext()).categoryDao() }
    single { AppDatabase.getInstance(androidContext()).auditDao() }
    single { AppDatabase.getInstance(androidContext()).questionDao() }
    single { AppDatabase.getInstance(androidContext()).bookmarkDao() }
}

val dataSourceModule = module {
    single { CategoriesLocalDataSource(get(), get()) }
    single { CategoriesRemoteDataSource(get()) }
    single { QuestionsLocalDataSource(get(), get(), get()) }
    single { QuestionsRemoteDataSource(get()) }
}

val repositoryModule = module {
    single { CategoriesRepository(get(), get()) }
    single { QuestionsRepository(get(), get()) }
}