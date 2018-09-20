package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Bookmarks which page to start the questions from for a given category and page size
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(

        @PrimaryKey
        @ColumnInfo(name = "category")
        val category: String,

        @ColumnInfo(name = "page")
        val page: Int = 1,

        @ColumnInfo(name = "page_size")
        val pageSize: Int = 10,

        @ColumnInfo(name = "last_page")
        val lastPage: Boolean = false
)