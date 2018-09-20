package com.asfour.data.persistence.dao

import android.arch.persistence.room.*
import com.asfour.data.persistence.entities.ChoiceEntity
import com.asfour.data.persistence.entities.QuestionEntity

@Dao
abstract class QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertQuestion(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertChoices(choice: List<ChoiceEntity>)

    @Transaction
    @Query("SELECT * FROM questions WHERE category = :category LIMIT :limit OFFSET :offset")
    abstract fun _findQuestionsByCategory(category: String, offset: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM choices WHERE questionId = :questionId")
    abstract fun _findChoicesByQuestionId(questionId: String): List<ChoiceEntity>

    @Query("DELETE FROM questions")
    abstract fun deleteAll()

    @Query("DELETE FROM questions WHERE category = :category")
    abstract fun deleteQuestionsForCategory(category: String)

    @Transaction
    open fun insert(questions: List<QuestionEntity>) {
        for (question in questions) {
            _insertChoices(question.choices)
            _insertQuestion(question)
        }
    }

    @Transaction
    open fun findQuestionsByCategory(category: String, page: Int, size: Int): List<QuestionEntity> {

        val offset = (if(page <= 0) 0 else page - 1) * size
        val questions = _findQuestionsByCategory(category, offset, size)
        for (question in questions) {
            question.choices = _findChoicesByQuestionId(question.id)
        }
        return questions
    }
}