package com.asfour.data.quiz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizScore(val score: Int = 0,
                     val maxScore: Int) : Parcelable{

    companion object {
        private val GRADES = arrayOf("D", "C", "B", "A", "A+")
    }

    val grade : String
        get() = GRADES[percentage.toInt() / 25]

    val percentage : Double
        get() = 100.0 * (this.score.toDouble() / this.maxScore.toDouble())
}