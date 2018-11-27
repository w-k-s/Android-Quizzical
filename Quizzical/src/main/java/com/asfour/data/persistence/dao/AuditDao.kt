package com.asfour.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.asfour.data.persistence.entities.AuditEntity

@Dao
interface AuditDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun auditEntity(audit: AuditEntity)

    @Query("DELETE FROM audits WHERE entity = :name")
    fun deleteAudit(name : String)

    @Query("SELECT COUNT(entity) > 0 as expired FROM audits WHERE entity = :name AND (last_modified_on + :maxElapsedSeconds) < CAST(strftime('%s', 'now') AS LONG)")
    fun isEntityExpired(name: String, maxElapsedSeconds: Long): Boolean
}