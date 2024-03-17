package com.example.vkandroidpasswordmanager.model.dto

import com.example.vkandroidpasswordmanager.model.db.enitity.WebsiteEntity

data class Website(
    val id: Long = 0L,
    val url: String = "",
    val name: String = url
) {
    fun entity() = WebsiteEntity(id, url, name)
}