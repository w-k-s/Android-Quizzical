package com.asfour.ui.categories

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.asfour.data.categories.source.CategoriesRepository
import com.asfour.utils.ConnectivityAssistant

class CategoriesViewModelFactory(val app: Application,
                                 val categoriesRepository: CategoriesRepository,
                                 val connectivityAssistant: ConnectivityAssistant) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoriesViewModel(app, categoriesRepository, connectivityAssistant) as T
    }
}