package com.example.vkandroidpasswordmanager.model.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val FILE_KEY = "prefs"
const val PASSWORD_KEY = "master_password"
const val ENCRYPT_KEY = "website_iv"

@Singleton
class PrefsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val preferences = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)

    fun getPassword() = preferences.getString(PASSWORD_KEY, null)
    fun setPassword(password: String) {
            preferences
                .edit()
                .putString(PASSWORD_KEY, password)
                .apply()
    }

//    map of pairs password id to iv bytes
//    (iv bytes used to decrypt password, stored in local db)
    @OptIn(ExperimentalEncodingApi::class)
    fun getIvPairs(): Map<Long, ByteArray>? {
        val json = preferences.getString(ENCRYPT_KEY, null)

        return Gson().fromJson<Map<Long, String>?>(
                json,
                object : TypeToken<Map<Long, String>>() {}.type
            )?.mapValues {
                Base64.decode(it.value.toByteArray())
            }
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun setIvPairs(map: Map<Long, ByteArray>) {
        val formattedMap = map.mapValues {
            Base64.encode(it.value)
        }
        preferences
            .edit()
            .putString(ENCRYPT_KEY, Gson().toJson(formattedMap))
            .apply()
    }
}