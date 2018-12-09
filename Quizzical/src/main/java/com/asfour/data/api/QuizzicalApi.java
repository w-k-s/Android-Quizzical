package com.asfour.data.api;

import com.asfour.data.categories.Categories;
import com.asfour.data.questions.Question;

import java.util.List;

import kotlinx.coroutines.Deferred;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Waqqas on 26/06/15.
 */
public interface QuizzicalApi {

    @GET("/prod/categories")
    Deferred<Categories> getCategories();

    @GET("/prod/questions")
    Deferred<Paginated<List<Question>>> getQuestions(
            @Query("category") String category,
            @Query("page") int page,
            @Query("size") int pageSize);
}
