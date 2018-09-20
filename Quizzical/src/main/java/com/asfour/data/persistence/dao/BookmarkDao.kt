package com.asfour.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.asfour.data.persistence.entities.BookmarkEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BookmarkDao {

    @Insert
    fun insert(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmarks WHERE category = :category LIMIT 1")
    fun findBookmarkByCategory(category: String): Maybe<BookmarkEntity>
}