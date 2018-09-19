package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.asfour.data.questions.Id
import com.asfour.data.questions.Question

@Entity(tableName = "questions")
data class QuestionEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "category")
        val category: String
) {
    @Ignore
    var choices: List<ChoiceEntity> = emptyList()

    constructor(question: Question) :
            this(question.id.id,
                    question.title,
                    question.category){
        this.choices = question.choices.map { ChoiceEntity(question.id.id, it) }
    }

    fun toQuestion() = Question(
            Id(this.id),
            this.title,
            this.category,
            this.choices.map { it.toChoice() }
    )
}