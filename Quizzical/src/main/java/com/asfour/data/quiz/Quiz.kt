package com.asfour.data.quiz

import android.os.Parcelable
import com.asfour.data.categories.Category
import com.asfour.data.questions.Question
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Quiz(val category: Category,
                val questions: List<Question> = emptyList()) : Parcelable, Iterator<Question> {

    private var score = 0
    private var currentQuestionIndex = -1

    init {
        reset()
    }

    fun shuffle() {
        Collections.shuffle(questions)
    }

    fun reset() {
        currentQuestionIndex = -1
        shuffle()
    }

    fun incrementScore() = ++score;

    fun finalScore() : QuizScore = QuizScore(score,questions.size)

    operator fun get(index: Int): Question? =
            questions[index]


    override fun hasNext(): Boolean =
            this.currentQuestionIndex + 1 < this.questions.size


    override fun next(): Question =
            this.questions[++currentQuestionIndex]

    fun current(): Question =
            if (currentQuestionIndex == -1) {
                next()
            } else {
                questions[currentQuestionIndex]
            }
}