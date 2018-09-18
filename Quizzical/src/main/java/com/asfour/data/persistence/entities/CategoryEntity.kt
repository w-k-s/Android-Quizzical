package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.asfour.data.categories.Category

@Entity(tableName = "categories")
data class CategoryEntity(@PrimaryKey @ColumnInfo(name = "title") val title: String) {

    constructor(category: Category) : this(category.title)

    fun toCategory() = Category(this.title)
}