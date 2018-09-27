package com.asfour.ui.quiz

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.asfour.data.categories.Category
import com.asfour.data.questions.source.QuestionsRepository
import com.asfour.utils.ConnectivityAssistant

class QuizViewModelFactory(val app: Application,
                           val category: Category,
                           val quizRepository: QuestionsRepository,
                           val connectivityAssistant: ConnectivityAssistant) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuizViewModel(app, category, quizRepository, connectivityAssistant) as T
    }
}