package com.example.vkandroidpasswordmanager.model.db.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "websites")
data class WebsiteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val url: String = "",
    val name: String = url,
    val passwords: Map<String, String> = emptyMap()
) {
    fun dto() = Website(id, url, name, passwords)
}

class Converter {
    @TypeConverter
    fun jsonFromPasswords(passwords: Map<String, String>): String =
        Gson().toJson(passwords)

    @TypeConverter
    fun passwordsFromJson(json: String): Map<String, String> =
        Gson().fromJson(
            json,
            object : TypeToken<Map<String, String>>() {}.type
        )
}