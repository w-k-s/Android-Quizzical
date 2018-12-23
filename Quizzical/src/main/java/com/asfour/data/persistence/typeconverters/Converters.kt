package com.asfour.data.persistence.typeconverters

import android.arch.persistence.room.TypeConverter
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it * 1000) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long {
        return date?.let { it.time / 1000 } ?: 0
    }

}