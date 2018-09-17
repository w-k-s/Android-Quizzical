package com.asfour.data.categories

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(@SerializedName("title") val title: String) : Parcelable

@Parcelize
data class Categories(@SerializedName("categories") val categories: List<Category>) : Parcelable