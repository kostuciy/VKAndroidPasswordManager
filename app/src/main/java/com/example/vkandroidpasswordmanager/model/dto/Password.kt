package com.example.vkandroidpasswordmanager.model.dto

import com.example.vkandroidpasswordmanager.model.db.enitity.PasswordEntity

data class Password(
    val id: Long = 0L,
    val websiteId: Long = 0L,
    val context: String = "",
    val password: String = ""
) {
    fun entity() = PasswordEntity(id, websiteId, context, password)
}