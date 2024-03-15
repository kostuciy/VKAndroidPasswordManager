package com.example.vkandroidpasswordmanager.model.repo

import com.example.vkandroidpasswordmanager.model.dto.Website
import kotlinx.coroutines.flow.Flow

interface Repository {
//    TODO: db
    val list: Flow<List<Website>>
//    suspend fun get(): Map<String, String>

    suspend fun save(website: Website)

    suspend fun delete(id: Long)

    suspend fun delete(idList: List<Long>)
    suspend fun deleteAll()
}