package com.asfour.ui.quiz

import com.asfour.data.categories.Category
import com.asfour.data.questions.Choice
import com.asfour.data.questions.source.QuestionsRepository
import com.asfour.data.quiz.Quiz
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuizPresenter(private var view: QuizContract.View?,
                    private val category: Category,
                    private val quizRepository: QuestionsRepository) : QuizContract.Presenter {

    private var quiz: Quiz = Quiz(category, emptyList())
    private val compositeDisposable = CompositeDisposable()

    override fun startQuiz() {
        view?.setProgressIndicator(true)

        compositeDisposable.add(quizRepository.loadQuestions(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    quiz = Quiz(category, it)
                    view?.setProgressIndicator(false)
                    view?.showQuestion(quiz.next())
                }, {
                    view?.setProgressIndicator(false)
                    view?.showError(it.message ?: "Unknown Error")
                }))
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
        compositeDisposable.dispose()
        view = null
    }
}