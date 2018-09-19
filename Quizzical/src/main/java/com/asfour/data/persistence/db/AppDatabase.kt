package com.asfour.data.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.asfour.data.persistence.dao.AuditDao
import com.asfour.data.persistence.dao.CategoryDao
import com.asfour.data.persistence.dao.QuestionDao
import com.asfour.data.persistence.entities.AuditEntity
import com.asfour.data.persistence.entities.CategoryEntity
import com.asfour.data.persistence.entities.ChoiceEntity
import com.asfour.data.persistence.entities.QuestionEntity
import com.asfour.data.persistence.typeconverters.Converters

@Database(
        version = 1,
        entities = [
            CategoryEntity::class,
            AuditEntity::class,
            QuestionEntity::class,
            ChoiceEntity::class
        ]
)
@TypeConverters(value = [
    Converters::class
])
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "quizzical_db").build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun categoryDao(): CategoryDao
    abstract fun auditDao() : AuditDao
    abstract fun questionDao() : QuestionDao
}