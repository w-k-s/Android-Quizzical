package com.asfour.data.questions

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Choice(@SerializedName("title") val title : String = "",
                  @SerializedName("correct") val correct: Boolean = false) : Parcelable