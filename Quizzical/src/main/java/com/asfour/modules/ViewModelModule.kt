package com.asfour.modules

import com.asfour.data.categories.Category
import com.asfour.ui.categories.CategoriesViewModel
import com.asfour.ui.quiz.QuizViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { CategoriesViewModel(androidApplication(), get(), get()) }
    viewModel { (category: Category) -> QuizViewModel(androidApplication(), category, get(), get()) }
}