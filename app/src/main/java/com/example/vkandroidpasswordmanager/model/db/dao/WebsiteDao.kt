package com.example.vkandroidpasswordmanager.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.vkandroidpasswordmanager.model.db.enitity.PasswordEntity
import com.example.vkandroidpasswordmanager.model.db.enitity.WebsiteEntity
import com.example.vkandroidpasswordmanager.model.db.enitity.WebsiteWithPasswordsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WebsiteDao {

    @Transaction
    @Query("SELECT * FROM websites")
    fun get(): Flow<List<WebsiteWithPasswordsEntity>>

    @Query("SELECT * FROM passwords WHERE websiteId=:id")
    suspend fun getPasswords(id: Long): List<PasswordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebsite(website: WebsiteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasswords(passwords: List<PasswordEntity>)

    @Query("DELETE FROM websites WHERE id IN (:idList)")
    suspend fun deleteWebsites(idList: List<Long>)

    @Query("DELETE FROM passwords WHERE websiteId=:websiteId")
    suspend fun deletePasswordsFromWebsite(websiteId: Long)

    @Query("DELETE FROM passwords WHERE id IN (:idList)")
    suspend fun deletePasswordsFromWebsites(idList: List<Long>)

    @Query("DELETE FROM passwords WHERE id IN (:idList)")
    suspend fun deletePasswords(idList: List<Long>)
}