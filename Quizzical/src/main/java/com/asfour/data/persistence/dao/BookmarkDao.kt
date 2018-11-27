package com.asfour.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.asfour.data.persistence.entities.BookmarkEntity

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmarks WHERE category = :category")
    fun findBookmarkByCategory(category: String): BookmarkEntity

    @Query("DELETE FROM bookmarks WHERE category = :category")
    fun deleteBookmarkForCategory(category: String)
}