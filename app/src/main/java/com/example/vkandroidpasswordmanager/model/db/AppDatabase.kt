package com.example.vkandroidpasswordmanager.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vkandroidpasswordmanager.model.db.dao.WebsiteDao
import com.example.vkandroidpasswordmanager.model.db.enitity.Converter
import com.example.vkandroidpasswordmanager.model.db.enitity.PasswordEntity
import com.example.vkandroidpasswordmanager.model.db.enitity.WebsiteEntity

@Database(
    entities = [WebsiteEntity::class, PasswordEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): WebsiteDao
}