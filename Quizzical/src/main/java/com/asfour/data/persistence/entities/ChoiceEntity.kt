package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.asfour.data.questions.Choice

@Entity(tableName = "choices")
data class ChoiceEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "correct")
        val correct: Boolean,

        @ColumnInfo(name = "questionId")
        val questionId: Long
) {

    constructor(questionId: Long, choice: Choice) : this(choice.id,choice.title, choice.correct, questionId)

    fun toChoice() = Choice(this.id, this.title, this.correct)
}