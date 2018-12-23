package com.asfour.data.persistence.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

interface Auditable {
    fun entityName(): String
}

@Entity(tableName = "audits")
class AuditEntity(
        @PrimaryKey
        @ColumnInfo(name = "entity")
        val auditable: String,

        @ColumnInfo(name = "last_modified_on")
        val date: Date = Date()
) {
    companion object {
        fun newInstance(auditable: Auditable) = AuditEntity(auditable.entityName())
    }
}
