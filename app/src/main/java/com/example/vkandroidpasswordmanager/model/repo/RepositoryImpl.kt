package com.example.vkandroidpasswordmanager.model.repo

import com.example.vkandroidpasswordmanager.model.db.dao.WebsiteDao
import com.example.vkandroidpasswordmanager.model.dto.Website
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: WebsiteDao
) : Repository {
    override val list: Flow<List<Website>> =
        dao.get().map { websiteEntities ->
            websiteEntities.map { it.dto() }
        }.flowOn(Dispatchers.Main)

    override suspend fun save(website: Website) {
        dao.insert(website.entity())
    }

    override suspend fun delete(id: Long) {
        dao.delete(listOf(id))
    }

    override suspend fun delete(idList: List<Long>) {
        dao.delete(idList)
    }

    override suspend fun deleteAll() {
        dao.clear()
    }
}