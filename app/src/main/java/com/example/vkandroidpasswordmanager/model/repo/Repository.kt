package com.example.vkandroidpasswordmanager.model.repo

import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.model.dto.WebsiteWithPasswords
import kotlinx.coroutines.flow.Flow

interface Repository {
    val list: Flow<List<WebsiteWithPasswords>>

    suspend fun getPasswords(websiteId: Long): List<Password>

    suspend fun save(websiteId: Long, passwords: List<Password>)

    suspend fun save(website: Website): Long

    suspend fun deleteWebsite(id: Long)

    suspend fun deletePasswords(idList: List<Long>)

    suspend fun deleteWebsites(idList: List<Long>)
}