package com.asfour.ui.quiz

import com.asfour.data.categories.Category
import com.asfour.data.questions.Choice
import com.asfour.data.questions.Question
import com.asfour.data.questions.source.QuestionsRepository
import com.asfour.data.quiz.Quiz

class QuizPresenter(private var view: QuizContract.View?,
                    val category: Category,
                    val quizRepository: QuestionsRepository) : QuizContract.Presenter {

    private var quiz: Quiz = Quiz(category, emptyList())

    override fun startQuiz() {
        view?.setProgressIndicator(true)

        quizRepository.loadQuestions(category, onSuccess = {

            quiz = Quiz(category, it)
            view?.setProgressIndicator(false)
            view?.showQuestion(quiz.next())

        }, onError = {

            view?.setProgressIndicator(false)
            view?.showError(it.message ?: "Unknown Error")

        })
    }

    override fun onQuestionAnswered(choice: Choice) {
        if (choice.correct) {
            quiz.incrementScore()
        }

        if (quiz.hasNext()) {
            view?.showQuestion(quiz.next())
        } else {
            view?.showScore(quiz.finalScore())
        }
    }

    override fun dropView() {
        view = null
    }
}