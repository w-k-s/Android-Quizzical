package com.asfour.ui.quiz

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.asfour.R
import com.asfour.data.categories.Category
import com.asfour.data.questions.Choice
import com.asfour.data.questions.Question
import com.asfour.data.questions.source.QuestionsRepository
import com.asfour.data.quiz.Quiz
import com.asfour.data.quiz.QuizScore
import com.asfour.utils.ConnectivityAssistant
import com.example.android.architecture.blueprints.todoapp.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuizViewModel(val app: Application,
                    private val category: Category,
                    private val quizRepository: QuestionsRepository,
                    private val connectivityAssistant: ConnectivityAssistant) : AndroidViewModel(app) {

    private var quiz: Quiz = Quiz(category, emptyList())
    private val compositeDisposable = CompositeDisposable()

    val loading = MutableLiveData<Boolean>()
    val loadingError = MutableLiveData<String>()
    val question = MutableLiveData<Question>()

    internal val score = SingleLiveEvent<QuizScore>()

    init {
        loading.value = true
        loadingError.value = ""
        question.value = Question()
    }

    fun startQuiz() {
        loading.value = true

        compositeDisposable.add(quizRepository.loadQuestions(category, ignoreExpiry = !connectivityAssistant.hasNetworkConnection())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    quiz = Quiz(category, it)
                    loading.value = false
                    loadingError.value = ""
                    question.value = quiz.next()
                }, {
                    loading.value = false
                    loadingError.value = app.getString(R.string.err_fetching_questions)
                }))
    }

    fun onQuestionAnswered(choice: Choice) {
        if (choice.correct) {
            quiz.incrementScore()
        }

        if (quiz.hasNext()) {
            question.value = quiz.next()
        } else {
            score.value = quiz.finalScore()
        }
    }
}