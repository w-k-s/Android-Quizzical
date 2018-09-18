package com.asfour.data.api;

import com.asfour.data.api.ApiResponse;
import com.asfour.data.categories.Categories;
import com.asfour.data.questions.Question;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Waqqas on 26/06/15.
 */
public interface QuizzicalApi {

    @GET("/api/v3/categories")
    Maybe<Categories> getCategories();

    @GET("/api/v3/questions")
    Call<ApiResponse<List<Question>>> getQuestions(
            @Query("category") String category,
            @Query("size") int pageSize);
}
