package com.asfour.data.api

import com.google.gson.annotations.SerializedName

data class ApiResponse<out T>(@SerializedName("data") val data: T,
                              @SerializedName("page") val page: Int,
                              @SerializedName("size") val size: Int,
                              @SerializedName("page_count") val pageCount: Int,
                              @SerializedName("last") val last: Boolean)