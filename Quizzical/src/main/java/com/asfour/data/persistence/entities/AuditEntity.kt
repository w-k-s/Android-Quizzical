package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "audits")
data class AuditEntity(
        @PrimaryKey
        @ColumnInfo(name = "entity")
        val entityName: String,

        @ColumnInfo(name = "last_modified_on")
        val date: Date = Date()
)