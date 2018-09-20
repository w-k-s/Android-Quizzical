package com.asfour.data.questions.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Category
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.BookmarkDao
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.persistence.entities.AuditEntity
import com.asfour.data.persistence.entities.BookmarkEntity
import com.asfour.data.persistence.entities.QuestionEntity
import com.asfour.data.questions.Question
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single


class QuestionsLocalDataSource(private val questionsDao: QuestionDao,
                               private val bookmarkDao: BookmarkDao,
                               private val auditDao: AuditDao) {

    companion object {
        const val EXPIRY_QUESTIONS = 2 *
    }

    fun saveQuestions(questions: List<Question>) = Completable.fromCallable {
        questions.first()?.let {
            val category = it.category
            questionsDao.deleteQuestionsForCategory(category)
            bookmarkDao.deleteBookmarkForCategory(category)
        }
        questionsDao.insert(questions.map { QuestionEntity(it) })
        auditDao.auditEntity(AuditEntity(QuestionEntity.TABLE_NAME))
    }

    fun fetchQuestions(category: Category, page: Int, size: Int): Single<List<Question>>
            = auditDao.isEntityExpired(QuestionEntity.TABLE_NAME, )
            .filter { !it }
            .toSingle()
            .flatMap { Single.fromCallable { questionsDao.findQuestionsByCategory(category.title, page, size) } }
            .filter { !it.isEmpty() }
            .map { it.map { it.toQuestion() } }
            .toSingle()

    fun fetchBookmark(category: Category): Maybe<BookmarkEntity> = bookmarkDao.findBookmarkByCategory(category.title)

    fun saveBookmark(bookmark: BookmarkEntity): Completable = Completable.fromCallable { bookmarkDao.insert(bookmark) }
}

class QuestionsRemoteDataSource(private val quizzicalApi: QuizzicalApi) {

    fun loadQuestions(category: Category, page: Int, size: Int) = quizzicalApi.getQuestions(category.title, page, size)
}

class QuestionsRepository(private val remoteSource: QuestionsRemoteDataSource,
                          private val localSource: QuestionsLocalDataSource) {

    private fun loadQuestionsRemotely(category: Category, bookmark: BookmarkEntity): Single<List<Question>> {
        return remoteSource.loadQuestions(category, bookmark.page, bookmark.pageSize)
                .flatMap {
                    val questions = it.data
                    val nextPage = if (it.page + 1 > it.pageCount) 1 else it.page + 1
                    val newBookmark = BookmarkEntity(category.title, nextPage, bookmark.pageSize, it.pageCount)
                    localSource.saveQuestions(questions)
                            .andThen(localSource.saveBookmark(newBookmark))
                            .andThen(Single.just(questions))
                }
    }

    private fun loadQuestionsLocally(category: Category, bookmark: BookmarkEntity): Single<List<Question>> {
        return localSource
                .fetchQuestions(category, bookmark.page, bookmark.pageSize)
                .flatMap {
                    val nextPage = if (bookmark.page + 1 > bookmark.pageCount) 1 else bookmark.page + 1
                    val newBookmark = BookmarkEntity(category.title, nextPage, bookmark.pageSize, bookmark.pageCount)
                    localSource.saveBookmark(newBookmark)
                            .andThen(Single.just(it))
                }
    }

    fun loadQuestions(category: Category): Single<List<Question>> {

        return localSource.fetchBookmark(category)
                .switchIfEmpty(Single.just(BookmarkEntity(category.title)))
                .flatMap {
                    val bookmark = it
                    loadQuestionsLocally(category, bookmark)
                            .onErrorResumeNext {
                                loadQuestionsRemotely(category, bookmark)
                            }
                }
                .filter { !it.isEmpty() }
                .toSingle()

    }
}