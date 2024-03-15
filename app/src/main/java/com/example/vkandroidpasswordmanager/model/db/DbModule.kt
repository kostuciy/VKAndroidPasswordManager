package com.example.vkandroidpasswordmanager.model.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun provideBd(@ApplicationContext context: Context): AppDatabase
            = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "websites_database"
    ).build()

    @Singleton
    @Provides
    fun provideDao(appDatabase: AppDatabase) = appDatabase.dao()
}