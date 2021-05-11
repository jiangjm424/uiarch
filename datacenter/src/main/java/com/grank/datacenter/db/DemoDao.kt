package com.grank.datacenter.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DemoDao {
    @get:Query("select * from demoentity")
    val reportItems: Flow<List<DemoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: DemoEntity)

    @Query("DELETE FROM demoentity WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM demoentity WHERE id = :id")
    suspend fun getReportItem(id: Int): DemoEntity?
}