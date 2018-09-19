package com.asfour.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.asfour.data.persistence.entities.CategoryEntity
import io.reactivex.Single

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(category: CategoryEntity)

    @Query("SELECT * FROM categories ORDER BY title ASC")
    fun list() : Single<List<CategoryEntity>>

    @Query("DELETE FROM categories")
    fun deleteAll()
}