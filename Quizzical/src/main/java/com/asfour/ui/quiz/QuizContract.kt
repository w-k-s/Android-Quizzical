package com.asfour.ui.quiz

import com.asfour.data.questions.Choice
import com.asfour.data.questions.Question
import com.asfour.data.quiz.QuizScore

interface QuizContract {

    interface View {
        fun setProgressIndicator(visible: Boolean)
        fun showQuestion(question: Question)
        fun showScore(quizScore: QuizScore)
        fun showError()
    }

    interface Presenter {
        fun startQuiz()
        fun onQuestionAnswered(choice: Choice)
        fun dropView()
    }
}