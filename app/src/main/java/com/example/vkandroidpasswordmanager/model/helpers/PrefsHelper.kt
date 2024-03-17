package com.example.vkandroidpasswordmanager.model.helpers

import android.content.Context
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val FILE_KEY = "prefs"
const val PASSWORD_KEY = "master_password"
const val IV_KEY = "website_iv"

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

//    map with websites' ids to ivs for decryption
//    by accessing ivs and enc passwords from db can decrypt to Password dtos
@OptIn(ExperimentalEncodingApi::class)
fun getIvMap(): Map<Long, ByteArray>? {
        val json = preferences.getString(IV_KEY, null)

        return Gson().fromJson<Map<Long, String>?>(
                json,
                object : TypeToken<Map<Long, String>>() {}.type
            )?.mapValues {
                Base64.decode(it.value.toByteArray())
            }
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun setIvMap(map: Map<Long, ByteArray>) {
        val formattedMap = map.mapValues {
//            String(it.value, StandardCharsets.UTF_8)
//            it.value.toString()
            Base64.encode(it.value)
        }
        preferences
            .edit()
            .putString(IV_KEY, Gson().toJson(formattedMap))
            .apply()
    }

//    map with passwords' ids to encrypted passwords to storing in db and decryption
//    fun getEncryptedMap(): Map<Long, ByteArray>? {
//        val json = preferences.getString(ENCRYPTED_KEY, null)
//
//        return Gson().fromJson(
//            json,
//            object : TypeToken<Map<Long, ByteArray>>() {}.type
//        )
//    }
//    fun setEncryptedMap(map: Map<Long, ByteArray>) {
//        preferences
//            .edit()
//            .putString(ENCRYPTED_KEY, Gson().toJson(map))
//            .apply()
//    }
}