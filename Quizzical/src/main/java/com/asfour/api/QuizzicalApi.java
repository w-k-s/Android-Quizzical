package com.asfour.api;

import com.asfour.api.params.CategoryParams;
import com.asfour.api.params.QuestionParams;
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
    public Observable<Categories> getCategories(@Query("token") final CategoryParams params);

    @GET("/api/questions")
    public Observable<Questions> getQuestions(@Query("token") final QuestionParams params);
}
