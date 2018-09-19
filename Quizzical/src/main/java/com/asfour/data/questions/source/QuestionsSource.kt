package com.asfour.data.questions.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Category
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.persistence.entities.QuestionEntity
import com.asfour.data.questions.Question
import io.reactivex.Completable
import io.reactivex.Single


class QuestionsLocalDataSource(private val questionsDao: QuestionDao) {

    fun saveQuestions(questions: List<Question>) = Completable.fromCallable {
        questionsDao.insert(questions.map { QuestionEntity(it) })
    }

    fun fetchQuestions(category: Category, page: Int, size: Int) : Single<List<Question>>
            = Single.fromCallable{ questionsDao.findQuestionsByCategory(category.title, page, size) }
            .filter{ !it.isEmpty() }
            .map { it.map { it.toQuestion() } }
            .toSingle()
}

class QuestionsRemoteDataSource(private val quizzicalApi: QuizzicalApi) {

    fun loadQuestions(category: Category, page: Int, size: Int) = quizzicalApi.getQuestions(category.title, page, size)
}

class QuestionsRepository(private val remoteSource: QuestionsRemoteDataSource,
                          private val localSource: QuestionsLocalDataSource) {

    fun loadQuestions(category: Category, page: Int = 1, size: Int = 10): Single<List<Question>> {

        val resumeSingleInCaseOfError =
                remoteSource.loadQuestions(category, page, size)
                        .map { it.data }
                        .flatMap { localSource.saveQuestions(it).andThen(Single.just(it)) }

        return localSource.fetchQuestions(category, page, size)
                .onErrorResumeNext(resumeSingleInCaseOfError)
    }
}