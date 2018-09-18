package com.asfour.data.questions.source

import com.asfour.data.api.QuizzicalApi
import com.asfour.data.api.ApiResponse
import com.asfour.data.categories.Category
import com.asfour.data.questions.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface QuestionsSource {
    fun loadQuestions(category: Category,
                      onSuccess: (List<Question>) -> Unit,
                      onError: (Throwable) -> Unit)
}

class QuestionsRemoteDataSource(val quizzicalApi: QuizzicalApi) : QuestionsSource {

    override fun loadQuestions(category: Category, onSuccess: (List<Question>) -> Unit, onError: (Throwable) -> Unit) {
        quizzicalApi.getQuestions(category.title,10).enqueue(object: Callback<ApiResponse<List<Question>>> {
            override fun onFailure(call: Call<ApiResponse<List<Question>>>?, t: Throwable?) {
                onError(t!!)
            }

            override fun onResponse(call: Call<ApiResponse<List<Question>>>?, response: Response<ApiResponse<List<Question>>>?) {
                onSuccess(response!!.body()!!.data)
            }

        })
    }
}

class QuestionsRepository(val remoteSource: QuestionsSource) : QuestionsSource {
    override fun loadQuestions(category: Category, onSuccess: (List<Question>) -> Unit, onError: (Throwable) -> Unit) {
        return remoteSource.loadQuestions(category, onSuccess, onError)
    }
}