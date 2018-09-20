package com.asfour.data.categories

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Category(@SerializedName("title") val title: String) : Parcelable, Comparable<Category>{
    override fun compareTo(other: Category): Int = title.compareTo(other.title)
}

@Parcelize
data class Categories(@SerializedName("categories") val categories: List<Category> = emptyList()) : Parcelable, Iterable<Category>{
    init {
        Collections.sort(categories)
    }
    val size get() = categories.size
    val isEmpty get() = categories.isEmpty()
    override fun iterator(): Iterator<Category> = categories.iterator()
    operator fun get(i : Int) = categories[i]
}