package com.example.vkandroidpasswordmanager.model.repo

import com.example.vkandroidpasswordmanager.model.db.dao.WebsiteDao
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.model.dto.WebsiteWithPasswords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: WebsiteDao
) : Repository {
    override val list: Flow<List<WebsiteWithPasswords>> =
        dao.get().map { websiteEntities ->
            websiteEntities.map { it.dto() }
        }.flowOn(Dispatchers.Main)

    override suspend fun saveEncrypted(passwords: List<Password>) {
        dao.insertPasswords(passwords.map { it.entity() })
    }

    override suspend fun save(website: Website, passwords: List<Password>): Long {
        val id = dao.insertWebsite(website.entity())
        dao.deletePasswordsFromWebsite(id)
        dao.insertPasswords(passwords.map { it.entity().copy(id = 0L, websiteId = id) })

        return id
    }


    override suspend fun deleteWebsite(id: Long) {
        dao.deletePasswordsFromWebsite(id)
        dao.deleteWebsites(listOf(id))
    }

    override suspend fun deleteWebsites(idList: List<Long>) {
        dao.deletePasswordsFromWebsites(idList)
        dao.deleteWebsites(idList)
    }

    override suspend fun deletePasswords(idList: List<Long>) {
        dao.deletePasswords(idList)
    }

    override suspend fun getPasswords(websiteId: Long): List<Password> =
        dao.getPasswords(websiteId).map { it.dto() }

}