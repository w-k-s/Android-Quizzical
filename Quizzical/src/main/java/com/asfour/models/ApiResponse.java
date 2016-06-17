package com.asfour.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by waqqas on 6/17/16.
 */
public class ApiResponse<T> {
    @SerializedName("Data")
    private T data;
    @SerializedName("CurrentPage")
    private int currentPage;
    @SerializedName("TotalPages")
    private int totalPages;
    @SerializedName("PageSize")
    private int pageSize;

    public T getData() {
        return data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }
}
