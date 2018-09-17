package com.asfour.ui.categories

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView

import com.asfour.R
import com.asfour.data.categories.Category

import butterknife.BindView
import butterknife.ButterKnife
import com.asfour.api.QuizzicalApi
import com.asfour.data.categories.Categories
import rx.android.app.AppObservable

/**
 * Created by Waqqas on 02/07/15.
 */
class CategoriesPresenter(private val view: CategoriesContract.View,
                          private val quizzicalApi: QuizzicalApi) : CategoriesContract.Presenter {


    private fun loadCategories() {

        view.setProgressIndicator(true)

        val categoriesObservable = quizzicalApi.categories

        mCategoriesSubscription = AppObservable.bindActivity(this, categoriesObservable)
                .subscribe({ categories ->
                    mCategories = categories
                    mCategoryListPresenter!!.hideProgressbar()
                    mCategoryListPresenter!!.showCategories(mCategories!!.categories)
                    subscriptions.remove(mCategoriesSubscription)
                }) { throwable ->
                    Log.e(CategoryListActivity.TAG, throwable.message, throwable)
                    mCategoryListPresenter!!.showError(getString(R.string.err_fetching_categories))
                    subscriptions.remove(mCategoriesSubscription)
                }
        subscriptions.add(mCategoriesSubscription!!)


    }
}
