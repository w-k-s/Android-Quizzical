package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.asfour.data.questions.Choice

@Entity(tableName = "choices")
data class ChoiceEntity(

        @PrimaryKey(autoGenerate = true)
        val id: Long,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "correct")
        val correct: Boolean,

        @ColumnInfo(name = "questionId")
        val questionId: String
) {

    constructor(questionId: String, choice: Choice) : this(0,choice.title, choice.correct, questionId)

    fun toChoice() = Choice(this.title, this.correct)
}