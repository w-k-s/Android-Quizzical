package com.asfour.di.modules

import android.content.Context
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.BookmarkDao
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.persistence.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesCategoryDao(context: Context): CategoryDao = AppDatabase.getInstance(context).categoryDao()

    @Provides
    @Singleton
    fun providesAuditDao(context: Context): AuditDao = AppDatabase.getInstance(context).auditDao()

    @Provides
    @Singleton
    fun provideQuestionDao(context: Context) : QuestionDao = AppDatabase.getInstance(context).questionDao()

    @Provides
    @Singleton
    fun provideBookmarkDao(context: Context) : BookmarkDao = AppDatabase.getInstance(context).bookmarkDao()
}