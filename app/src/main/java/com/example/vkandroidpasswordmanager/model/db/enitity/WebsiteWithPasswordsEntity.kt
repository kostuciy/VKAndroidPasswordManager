package com.example.vkandroidpasswordmanager.model.db.enitity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.vkandroidpasswordmanager.model.dto.WebsiteWithPasswords

data class WebsiteWithPasswordsEntity(
    @Embedded val website: WebsiteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "websiteId"
    )
    val passwords: List<PasswordEntity>
) {
    fun dto() = WebsiteWithPasswords(
        website.dto(),
        passwords.map { it.dto() }
    )
}