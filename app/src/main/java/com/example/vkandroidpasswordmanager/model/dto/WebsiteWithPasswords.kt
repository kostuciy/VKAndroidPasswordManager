package com.example.vkandroidpasswordmanager.model.dto

import com.example.vkandroidpasswordmanager.model.db.enitity.WebsiteWithPasswordsEntity

data class WebsiteWithPasswords(
    val website: Website,
    val passwords: List<Password>
) {
    fun entity() = WebsiteWithPasswordsEntity(
        website.entity(),
        passwords.map { it.entity() }
    )
}