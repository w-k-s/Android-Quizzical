package com.asfour.api;

import com.asfour.models.Categories;
import com.asfour.models.Questions;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Waqqas on 26/06/15.
 */
public interface QuizzicalApi {

    @GET("/categories?format=xml")
    public Observable<Categories> getCategories();

    @GET("/questions")
    public Observable<Questions> getQuestions(@Query("category") final String category);
}
