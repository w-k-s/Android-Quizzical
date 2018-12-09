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

        /**
         * The page to start reading from
         */
        @ColumnInfo(name = "page")
        val pageToLoad: Int = 1,

        @ColumnInfo(name = "page_size")
        val pageSize: Int = 10,

        @ColumnInfo(name = "page_count")
        val pageCount: Int = 1
) {
    fun nextBookmark(): BookmarkEntity {
        val nexPage = if (pageToLoad + 1 > pageCount) 1 else pageToLoad + 1
        return copy(pageToLoad = nexPage)
    }
}