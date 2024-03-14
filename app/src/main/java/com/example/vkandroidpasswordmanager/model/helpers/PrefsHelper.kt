package com.example.vkandroidpasswordmanager.model.helpers

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

const val FILE_KEY = "prefs"
const val PASSWORD_KEY = "master_password"

@Singleton
class PrefsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val preferences = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)

    var password: String?
        get() = preferences.getString(PASSWORD_KEY, null)
        set(value) {
            preferences
                .edit()
                .putString(value, null)
                .apply()
        }
}