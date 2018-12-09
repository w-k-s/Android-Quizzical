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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuizViewModel(val app: Application,
                    private val category: Category,
                    private val quizRepository: QuestionsRepository,
                    private val connectivityAssistant: ConnectivityAssistant) : AndroidViewModel(app) {

    private var quiz: Quiz? = null

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
        if(quiz == null){
            loadQuestions()
        }else{
            resumeQuiz()
        }
    }

    private fun resumeQuiz(){
        //triggers display of current question on ui
        question.value = question.value
    }

    private fun loadQuestions() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                loading.value = true
                val hasInternetConnection = connectivityAssistant.hasInternetConnection()
                val questions = quizRepository.loadQuestions(category, ignoreExpiry = !hasInternetConnection)
                quiz = Quiz(category, questions)
                question.value = quiz!!.next()
            } catch (e: Exception) {
                loadingError.value = app.getString(R.string.err_fetching_questions)
            } finally {
                loading.value = false
            }
        }
    }

    fun onQuestionAnswered(choice: Choice) {
        quiz?.let { quiz ->
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
}