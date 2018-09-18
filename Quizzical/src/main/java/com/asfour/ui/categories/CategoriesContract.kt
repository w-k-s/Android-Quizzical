package com.asfour.ui.categories

import com.asfour.data.categories.Categories
import com.asfour.data.categories.Category

interface CategoriesContract {
    interface View{
        fun setProgressIndicator(visible: Boolean)
        fun showCategories(categories: Categories)
        fun showError(message: String)
        fun startQuiz(category: Category)
    }

    interface Presenter{
        fun loadCategories()
        fun onCategorySelected(category: Category)
        fun dropView()
    }
}