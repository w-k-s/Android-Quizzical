package com.asfour.ui.categories

import com.asfour.data.categories.Categories

interface CategoriesContract {
    interface View{
        fun setProgressIndicator(visible: Boolean)
    }

    interface Presenter{
        fun loadCategories(callback: (Categories)->Unit)
    }
}