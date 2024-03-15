package com.example.vkandroidpasswordmanager.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vkandroidpasswordmanager.model.db.enitity.WebsiteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WebsiteDao {

    //    as db only has 1 page of product data,
//    it actually gets only 20 objects, not all
    @Query("SELECT * FROM websites")
    fun get(): Flow<List<WebsiteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: WebsiteEntity)

    @Query("DELETE FROM websites WHERE id IN (:idList)")
    suspend fun delete(idList: List<Long>)

    @Query("DELETE FROM websites")
    suspend fun clear()
}