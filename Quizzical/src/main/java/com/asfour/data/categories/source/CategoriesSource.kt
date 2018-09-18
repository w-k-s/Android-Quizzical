package com.asfour.data.categories.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.categories.Categories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CategoriesDataSource {
    fun loadCategories(onSuccess: (Categories) -> Unit,
                       onError: (Throwable) -> Unit)
}

class CategoriesRemoteDataSource(private val api: QuizzicalApi) : CategoriesDataSource {

    override fun loadCategories(onSuccess: (Categories) -> Unit, onError: (Throwable) -> Unit) {
        api.categories.enqueue(object: Callback<Categories>{
            override fun onFailure(call: Call<Categories>?, t: Throwable?) {
                onError(t!!)
            }

            override fun onResponse(call: Call<Categories>?, response: Response<Categories>?) {
                onSuccess(response!!.body()!!)
            }
        })
    }
}

class CategoriesRepository(private val remoteSource: CategoriesDataSource) : CategoriesDataSource {
    override fun loadCategories(onSuccess: (Categories) -> Unit, onError: (Throwable) -> Unit) = remoteSource.loadCategories(onSuccess, onError)
}