package com.example.vkandroidpasswordmanager.model.db.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "websites")
data class WebsiteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val url: String = "",
    val name: String = url
) {
    fun dto() = Website(id, url, name)
}

class Converter { // TODO: remove
    @TypeConverter
    fun jsonFromPasswords(passwords: List<Password>): String =
        Gson().toJson(passwords)

    @TypeConverter
    fun passwordsFromJson(json: String): List<Password> =
        Gson().fromJson(
            json,
            object : TypeToken<List<Password>>() {}.type
        )
}