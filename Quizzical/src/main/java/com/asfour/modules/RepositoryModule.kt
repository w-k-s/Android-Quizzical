package com.asfour.modules

import com.asfour.data.categories.source.CategoriesRemoteDataSource
import com.asfour.data.categories.source.CategoriesRepository
import com.asfour.data.questions.source.QuestionsRemoteDataSource
import com.asfour.data.questions.source.QuestionsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesCategoriesRepository(remoteDataSource: CategoriesRemoteDataSource) = CategoriesRepository(remoteDataSource)

    @Provides
    @Singleton
    fun providesQuestionsRepository(remoteDataSource: QuestionsRemoteDataSource) = QuestionsRepository(remoteDataSource)
}