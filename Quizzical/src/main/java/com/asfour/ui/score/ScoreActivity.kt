package com.asfour.ui.score

import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import com.asfour.Extras
import com.asfour.R
import com.asfour.data.quiz.QuizScore
import com.asfour.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_score.*

/**
 * Displays the users score as a percentage.
 *
 * @author Waqqas
 */
class ScoreActivity : BaseActivity() {
    private var quizScore: QuizScore? = null

    private val colors: IntArray by lazy { resources.getIntArray(R.array.grade_colors) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.layout_score)

        if (intent.extras != null) {
            quizScore = intent.extras!!.getParcelable(Extras.Score)
        }

        if (quizScore == null) {
            finish()
        }

        showScore(quizScore)
    }

    fun showScore(score: QuizScore?) {

        var percentage = score!!.percentage
        if (percentage == 0.0) percentage = 0.5

        val animateScore = ScoreAnimation(score,
                (percentage * MILLISECONDS_PER_TICK.toDouble() * 1.4).toLong(),
                MILLISECONDS_PER_TICK)

        animateScore.start()
    }

    inner class ScoreAnimation(private val mQuizScore: QuizScore, millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        private var counter = 0

        override fun onFinish() {
            animateGradeStamp(mQuizScore.grade)
        }

        override fun onTick(millisUntilFinished: Long) {
            if (counter <= mQuizScore.percentage.toInt()) {
                showPercentageWithColor(counter)
                counter++
            }
        }
    }

    private fun showPercentageWithColor(percentage: Int) {
        scoreTextView.setTextColor(colors[percentage / (MAX_PERCENTAGE / (colors.size - 1))])
        scoreTextView.text = "$percentage%"
    }

    private fun animateGradeStamp(grade: String) {

        gradeTextView.text = String.format("[ %s ]", grade)

        val scoreAnimation = AnimationUtils.loadAnimation(this, R.anim.score)

        gradeTextView.startAnimation(scoreAnimation)
    }

    companion object {
        private const val MAX_PERCENTAGE = 100
        private const val MILLISECONDS_PER_TICK: Long = 50
    }
}
