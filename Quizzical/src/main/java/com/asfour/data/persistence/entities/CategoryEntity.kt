package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.asfour.data.categories.Category
import com.asfour.data.persistence.entities.CategoryEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CategoryEntity(

        @PrimaryKey
        @ColumnInfo(name = "title")
        val title: String) {

    constructor(category: Category) : this(category.title)

    fun toCategory() = Category(this.title)

    companion object {
        const val TABLE_NAME = "categories"
    }
}