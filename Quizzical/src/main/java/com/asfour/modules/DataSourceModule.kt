package com.asfour.modules

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.source.CategoriesLocalDataSource
import com.asfour.data.categories.source.CategoriesRemoteDataSource
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.questions.source.QuestionsRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataSourceModule {

    @Provides
    fun provideCategoriesLocalDataSource(dao : CategoryDao) = CategoriesLocalDataSource(dao)

    @Provides
    fun provideCategoriesRemoteDataSource(api: QuizzicalApi) = CategoriesRemoteDataSource(api)

    @Provides
    fun provideQuestionsDataSource(api: QuizzicalApi) = QuestionsRemoteDataSource(api)
}