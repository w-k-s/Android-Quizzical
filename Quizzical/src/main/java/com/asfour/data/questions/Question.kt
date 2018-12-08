package com.asfour.data.questions

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
        @SerializedName("id")
        val id : Long = 0,

        @SerializedName("question")
        val title: String = "",

        @SerializedName("category")
        val category: String = "",

        @SerializedName("choices")
        val choices: List<Choice> = List(4) { Choice() }

) : Parcelable