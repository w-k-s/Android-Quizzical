package com.asfour.api;
import com.asfour.models.ApiResponse;
import com.asfour.models.Category;
import com.asfour.models.Question;
import com.asfour.models.Token;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Waqqas on 26/06/15.
 */
public interface QuizzicalApi {


    @POST("/api/v2/auth?sub=com.asfour.Quizzical")
    public Observable<ApiResponse<Token>> getSessionToken(@Body Object body);

    @GET("/api/v2/categories")
    public Observable<ApiResponse<List<Category>>> getCategories();

    @GET("/api/v2/questions")
    public Observable<ApiResponse<List<Question>>> getQuestions(
            @Query("category") String category,
            @Query("size") int pageSize);
}
