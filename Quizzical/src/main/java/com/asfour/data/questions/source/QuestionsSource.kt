package com.asfour.data.questions.source

import com.asfour.data.api.ApiResponse
import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Category
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.BookmarkDao
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.persistence.entities.AuditEntity
import com.asfour.data.persistence.entities.BookmarkEntity
import com.asfour.data.persistence.entities.QuestionEntity
import com.asfour.data.questions.Question
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit


class QuestionsLocalDataSource(private val questionsDao: QuestionDao,
                               private val bookmarkDao: BookmarkDao,
                               private val auditDao: AuditDao) {

    suspend fun saveQuestions(questions: List<Question>) {
        return GlobalScope.async {
            questions.first()?.let {
                val category = it.category
                questionsDao.deleteQuestionsForCategory(category)
                bookmarkDao.deleteBookmarkForCategory(category)
            }
            questionsDao.insert(questions.map { QuestionEntity(it) })
            auditDao.auditEntity(AuditEntity(QuestionEntity.TABLE_NAME))
        }.await()
    }

    suspend fun fetchQuestions(category: Category, page: Int, size: Int, ignoreExpiry: Boolean): List<Question> {
        return GlobalScope.async {
            val expired = auditDao.isEntityExpired(QuestionEntity.TABLE_NAME, TimeUnit.DAYS.toSeconds(7))

            if (!ignoreExpiry && expired) {
                emptyList()
            } else {
                questionsDao.findQuestionsByCategory(category.title, page, size).map { it.toQuestion() }
            }

        }.await()
    }

    suspend fun fetchBookmark(category: Category) = GlobalScope.async { bookmarkDao.findBookmarkByCategory(category.title) }.await()

    suspend fun saveBookmark(bookmark: BookmarkEntity) = GlobalScope.async { bookmarkDao.insert(bookmark) }.await()
}

class QuestionsRemoteDataSource(private val quizzicalApi: QuizzicalApi) {

    suspend fun loadQuestions(category: Category, page: Int, size: Int): ApiResponse<List<Question>> = quizzicalApi.getQuestions(category.title, page, size).await()
}

class QuestionsRepository(private val remoteSource: QuestionsRemoteDataSource,
                          private val localSource: QuestionsLocalDataSource) {

    private suspend fun loadQuestionsRemotely(category: Category, bookmark: BookmarkEntity): List<Question> {
        return GlobalScope.async {
            val response = remoteSource.loadQuestions(category, bookmark.page, bookmark.pageSize)

            val questions = response.data
            val nextPage = if (response.page + 1 > response.pageCount) 1 else response.page + 1
            val newBookmark = BookmarkEntity(category.title, nextPage, bookmark.pageSize, response.pageCount)

            localSource.saveQuestions(questions)
            localSource.saveBookmark(newBookmark)

            questions
        }.await()

    }

    private suspend fun loadQuestionsLocally(category: Category, bookmark: BookmarkEntity, ignoreExpiry: Boolean): List<Question> {
        return GlobalScope.async {

            val questions = localSource.fetchQuestions(category, bookmark.page, bookmark.pageSize, ignoreExpiry)
            val nextPage = if (bookmark.page + 1 > bookmark.pageCount) 1 else bookmark.page + 1
            val newBookmark = BookmarkEntity(category.title, nextPage, bookmark.pageSize, bookmark.pageCount)
            localSource.saveBookmark(newBookmark)
            questions
        }.await()
    }

    suspend fun loadQuestions(category: Category,
                              ignoreExpiry: Boolean = false): List<Question> {

        return GlobalScope.async {
            var bookmark = localSource.fetchBookmark(category)
            if (bookmark == null) {
                bookmark = BookmarkEntity(category.title)
            }

            var questions = loadQuestionsLocally(category, bookmark, ignoreExpiry)

            if (questions.isEmpty()) {
                loadQuestionsRemotely(category, bookmark)
            } else {
                questions
            }
        }.await()

    }
}