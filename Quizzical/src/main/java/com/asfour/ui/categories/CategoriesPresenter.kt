package com.asfour.ui.categories

import com.asfour.data.categories.Categories
import com.asfour.data.categories.Category
import com.asfour.data.categories.source.CategoriesRepository

/**
 * Created by Waqqas on 02/07/15.
 */
class CategoriesPresenter(private var view: CategoriesContract.View?,
                          private val categoriesRespository: CategoriesRepository) : CategoriesContract.Presenter {

    override fun loadCategories() {

        view?.setProgressIndicator(true)

        categoriesRespository.loadCategories(onSuccess = {

            view?.setProgressIndicator(false)
            view?.showCategories(it)

        }, onError = {

            view?.setProgressIndicator(false)
            view?.showError(it.message ?: "")

        })
    }

    override fun onCategorySelected(category: Category) {
        view?.startQuiz(category)
    }

    override fun dropView() {
        view = null
    }
}
