package com.asfour.modules

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.source.CategoriesRemoteDataSource
import com.asfour.data.questions.source.QuestionsRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataSourceModule {

    @Provides
    fun provideCategoriesDataSource(api: QuizzicalApi): CategoriesRemoteDataSource = CategoriesRemoteDataSource(api)

    @Provides
    fun provideQuestionsDataSource(api: QuizzicalApi): QuestionsRemoteDataSource = QuestionsRemoteDataSource(api)
}