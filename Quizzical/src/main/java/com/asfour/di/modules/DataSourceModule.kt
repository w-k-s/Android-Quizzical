package com.asfour.di.modules

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.source.CategoriesLocalDataSource
import com.asfour.data.categories.source.CategoriesRemoteDataSource
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.BookmarkDao
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.questions.source.QuestionsLocalDataSource
import com.asfour.data.questions.source.QuestionsRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataSourceModule {

    @Provides
    fun provideCategoriesLocalDataSource(categoryDao: CategoryDao,
                                         auditDao: AuditDao) = CategoriesLocalDataSource(categoryDao, auditDao)

    @Provides
    fun provideCategoriesRemoteDataSource(api: QuizzicalApi) = CategoriesRemoteDataSource(api)

    @Provides
    fun provideQuestionsLocalDataSource(questionDao: QuestionDao,
                                        bookmarkDao: BookmarkDao,
                                        auditDao: AuditDao) = QuestionsLocalDataSource(questionDao,bookmarkDao,auditDao)

    @Provides
    fun provideQuestionsDataSource(api: QuizzicalApi) = QuestionsRemoteDataSource(api)
}