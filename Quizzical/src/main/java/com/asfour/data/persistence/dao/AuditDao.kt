package com.asfour.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.asfour.data.persistence.entities.AuditEntity
import com.asfour.data.persistence.entities.Auditable

@Dao
abstract class AuditDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun auditEntity(audit: AuditEntity)

    @Query("DELETE FROM audits WHERE entity = :name")
    abstract fun deleteAudit(name: String)

    @Query("SELECT COUNT(entity) > 0 as expired FROM audits WHERE entity = :name AND (last_modified_on + :maxElapsedSeconds) < CAST(strftime('%s', 'now') AS LONG)")
    abstract fun isEntityExpired(name: String, maxElapsedSeconds: Long): Boolean

    fun auditEntity(auditable: Auditable) = auditEntity(AuditEntity.newInstance(auditable))
}