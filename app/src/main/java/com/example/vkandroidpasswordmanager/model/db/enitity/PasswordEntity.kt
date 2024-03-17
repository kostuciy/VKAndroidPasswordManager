package com.example.vkandroidpasswordmanager.model.db.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vkandroidpasswordmanager.model.dto.Password

@Entity(tableName = "passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val websiteId: Long = 0L,
    val context: String = "",
    val password: String = ""
) {
    fun dto() = Password(id, websiteId, context, password)
}