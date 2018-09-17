package com.asfour.data.categories.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.asfour.api.QuizzicalApi
import com.asfour.data.categories.Categories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CategoriesDataSource {
    fun loadCategories(): LiveData<Categories>
}

class CategoriesRemoteDataSource(val api: QuizzicalApi) : CategoriesDataSource {

    override fun loadCategories(): LiveData<Categories> {

        val data = MutableLiveData<Categories>()
        data.

        api.categories.enqueue(object: Callback<Categories>{
            override fun onFailure(call: Call<Categories>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Categories>?, response: Response<Categories>?) {

            }
        })
    }
}

class CategoriesRepository(val remoteSource: CategoriesDataSource) : CategoriesDataSource {
    override fun loadCategories(): LiveData<Categories> = remoteSource.loadCategories()
}