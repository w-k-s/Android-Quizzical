package com.asfour.data.questions.source

import com.asfour.data.api.Paginated
import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Category
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.BookmarkDao
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.persistence.entities.BookmarkEntity
import com.asfour.data.persistence.entities.QuestionEntity
import com.asfour.data.questions.Question
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.TimeUnit


class QuestionsLocalDataSource(private val questionsDao: QuestionDao,
                               private val bookmarkDao: BookmarkDao,
                               private val auditDao: AuditDao) {

    suspend fun saveQuestions(questions: List<Question>, page: Int) {
        if (questions.isEmpty()) {
            return
        }

        return GlobalScope.async {

            val category = questions.first().category
            val entities = questions.map { QuestionEntity(it) }

            if (page == 1) {
                //if loading questions on page 1, delete all exiting questions
                questionsDao.deleteQuestionsForCategory(category)
                bookmarkDao.deleteBookmarkForCategory(category)
            }

            questionsDao.insert(entities)
            auditDao.auditEntity(entities.first())

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

    suspend fun loadQuestions(category: Category, page: Int, size: Int): Paginated<List<Question>> = quizzicalApi.getQuestions(category.title, page, size).await()
}

class QuestionsRepository(private val remoteSource: QuestionsRemoteDataSource,
                          private val localSource: QuestionsLocalDataSource) {

    private suspend fun loadQuestionsRemotely(category: Category, bookmark: BookmarkEntity): List<Question> {
        return GlobalScope.async {
            val response = remoteSource.loadQuestions(category, bookmark.pageToLoad, bookmark.pageSize)

            val questions = response.data
            val newBookmark = bookmark.copy(pageToLoad = response.nextPage, pageCount = response.pageCount)

            localSource.saveQuestions(questions, response.page)
            localSource.saveBookmark(newBookmark)

            questions
        }.await()

    }

    private suspend fun loadQuestionsLocally(category: Category, bookmark: BookmarkEntity, ignoreExpiry: Boolean): List<Question> {
        return GlobalScope.async {

            val questions = localSource.fetchQuestions(category, bookmark.pageToLoad, bookmark.pageSize, ignoreExpiry)
            localSource.saveBookmark(bookmark.nextBookmark())
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