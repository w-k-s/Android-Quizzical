package com.asfour.ui.quiz

import com.asfour.data.categories.Category
import com.asfour.data.questions.Choice
import com.asfour.data.questions.Question
import com.asfour.data.quiz.QuizScore

interface QuizContract {

    interface View {
        fun setProgressIndicator(visible: Boolean)
        fun showQuestion(question: Question)
        fun showScore(quizScore: QuizScore)
        fun showError(message: String)
    }

    interface Presenter {
        fun startQuiz()
        fun onQuestionAnswered(choice: Choice)
        fun dropView()
    }
}