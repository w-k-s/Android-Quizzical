package com.asfour.api;

import com.asfour.api.params.CategoryParameters;
import com.asfour.api.params.QuestionParameters;
import com.asfour.models.Categories;
import com.asfour.models.Questions;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Waqqas on 26/06/15.
 */
public interface QuizzicalApi {


    @GET("/api/categories")
    public Observable<Categories> getCategories(@Query("token") CategoryParameters params);

    @GET("/api/questions")
    public Observable<Questions> getQuestions(@Query("token") QuestionParameters params);
}
