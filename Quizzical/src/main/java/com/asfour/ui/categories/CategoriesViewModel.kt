package com.asfour.ui.categories

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.asfour.R
import com.asfour.data.categories.Categories
import com.asfour.data.categories.Category
import com.asfour.data.categories.source.CategoriesRepository
import com.asfour.utils.ConnectivityAssistant
import com.example.android.architecture.blueprints.todoapp.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Waqqas on 02/07/15.
 */
class CategoriesViewModel(private val app: Application,
                          private val categoriesRepository: CategoriesRepository,
                          private val connectivity: ConnectivityAssistant) : AndroidViewModel(app) {

    private val disposable = CompositeDisposable()

    val loading = MutableLiveData<Boolean>()
    val loadingError = MutableLiveData<String>()
    val categories = MutableLiveData<Categories>()

    internal val startQuiz = SingleLiveEvent<Category>()

    init {
        loading.value = true
        loadingError.value = ""
        categories.value = Categories()
    }

    fun loadCategories() {

        loading.value = true

        disposable.add(categoriesRepository.categories(ignoreExpiry = !connectivity.hasNetworkConnection())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loading.value = false
                    categories.value = it
                }, {
                    loading.value = false
                    loadingError.value = app.getString(R.string.err_fetching_categories)
                }))
    }
}
