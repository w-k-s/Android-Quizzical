package com.asfour.ui.categories

import com.asfour.data.categories.Category
import com.asfour.data.categories.source.CategoriesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Waqqas on 02/07/15.
 */
class CategoriesPresenter(private var view: CategoriesContract.View?,
                          private val categoriesRepository: CategoriesRepository) : CategoriesContract.Presenter {

    private val disposable = CompositeDisposable()

    override fun loadCategories() {

        view?.setProgressIndicator(true)

        disposable.add(categoriesRepository.categories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
            view?.setProgressIndicator(false)
            view?.showCategories(it)
        },{
            view?.showError(it?.message ?: "Unknwon Error")
        }))
    }

    override fun onCategorySelected(category: Category) {
        view?.startQuiz(category)
    }

    override fun dropView() {
        disposable.dispose()
        view = null
    }
}
