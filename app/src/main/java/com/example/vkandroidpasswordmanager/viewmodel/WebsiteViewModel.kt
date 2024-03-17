package com.example.vkandroidpasswordmanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.model.helpers.EncryptionHelper
import com.example.vkandroidpasswordmanager.model.helpers.PrefsHelper
import com.example.vkandroidpasswordmanager.model.repo.Repository
import com.example.vkandroidpasswordmanager.model.state.CurrentDataState
import com.example.vkandroidpasswordmanager.model.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@HiltViewModel
class WebsiteViewModel @Inject constructor(
    private val repository: Repository,
    private val prefsHelper: PrefsHelper,
    private val encryptionHelper: EncryptionHelper
) : ViewModel() {
    private var _state = MutableStateFlow(ListState())
    val state // TODO: show popup on error
        get() = _state

    private val _currentData = MutableStateFlow(CurrentDataState())
    val currentData: StateFlow<CurrentDataState>
        get() = _currentData

    val list = repository.list

//
//    private var _currentWebsite = Website()
//    val currentWebsite
//        get() = _currentWebsite
//
//    private var _currentPassword = Password()
//    val currentPassword
//        get() = _currentPassword

    fun select(website: Website, withPasswords: Boolean = true) {
//        _currentWebsite = website
//        if (withPasswords) getPasswords(website.id)
        _currentData.value = _currentData.value.copy(
            website = website
        )
        if (withPasswords) getPasswords(website.id)
    }

    fun select(password: Password) {
//        _currentPassword = password
        _currentData.value = _currentData.value.copy(password = password)
    }

    fun selectPasswordToEmpty() {
//        _currentPassword = Password()
        _currentData.value = _currentData.value.copy(password = Password())
    }

    fun selectToEmpty() {
//        _currentWebsite = Website()
//        _currentWebsitePasswords.value = emptyList()
//        selectPasswordToEmpty()
        _currentData.value = _currentData.value.copy(
            website = Website(),
            passwords = emptyList(),
            password = Password()
        )
    }

//    private var _currentWebsitePasswords =
//        MutableStateFlow<List<Password>>(emptyList())

//    val currentWebsitePasswords
//        get() = _currentWebsitePasswords

    fun addPassword() {
        val passwords = _currentData.value.passwords
        val currentPassword = _currentData.value.password
        _currentData.value = _currentData.value.copy(
            passwords =
                if (currentPassword.id in passwords.map { it.id })
                    passwords.map { if (it.id == currentPassword.id) currentPassword else it }
                else passwords + currentPassword
        )
    }

    fun removePassword(id: Long) {
//        val passwords = _currentWebsitePasswords.value
//        _currentWebsitePasswords.value = passwords.filter { it.id != id }
        val passwords = _currentData.value.passwords
        _currentData.value = _currentData.value.copy(
            passwords = passwords.filter { it.id != id }
        )
    }

    fun save() {
        viewModelScope.launch {
//            try {
                _state.value = ListState(loading = true)
                val websiteId = repository.save(_currentData.value.website)
                val encryptedPasswords = encryptPasswords(
                    websiteId,
                    _currentData.value.passwords
                )
                repository.save(websiteId, encryptedPasswords)
                _state.value = ListState()
//            } catch (e: Exception) {
//                _state.value = ListState(error = true)
//            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            try {
                _state.value = ListState(loading = true)
                repository.deleteWebsite(id)
                _state.value = ListState()
            } catch (e: Exception) {
                _state.value = ListState(error = true)
            }
        }
    }

    private fun getPasswords(websiteId: Long) {
        viewModelScope.launch {
//            try {
                _state.value = ListState(loading = true)
//                _currentWebsitePasswords.value =
                val passwords = repository.getPasswords(websiteId)
                val decryptedPasswords = decryptPasswords(
                    websiteId,
                    passwords
                )
                _currentData.value = _currentData.value.copy(
                    passwords = decryptedPasswords
                )
                _state.value = ListState()
//            } catch (e: Exception) {
//                _state.value = ListState(error = true)
//            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun encryptPasswords(websiteId: Long, passwords: List<Password>): List<Password> {
        val ivMap = prefsHelper.getIvMap() ?: emptyMap()
        lateinit var ivBytes: ByteArray

        Log.d("GAG", "${passwords}")
        if (passwords.isEmpty()) return emptyList()
        val encryptedPasswords = passwords.map { password ->
            val ivToPassword = encryptionHelper.encryptData(
                password.password.toByteArray()
            )
            ivBytes = ivToPassword.first
            password.copy(password =
//            String(ivToPassword.second, StandardCharsets.UTF_8)
            Base64.encode(ivToPassword.second)
            )
        }

        prefsHelper.setIvMap(ivMap + Pair(websiteId, ivBytes))
        Log.d("GAG", "${encryptedPasswords}")
        return encryptedPasswords
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decryptPasswords(websiteId: Long, passwords: List<Password>): List<Password> {
        Log.d("GAG", "${passwords}")
        val ivMap = prefsHelper.getIvMap() ?: emptyMap()
        val ivBytes = ivMap[websiteId] ?: return emptyList()

        val decryptedPasswords = passwords.map { password ->
            val decryptedBytes = encryptionHelper.decryptData(
                Base64.decode(password.password.toByteArray()), ivBytes
            )
            password.copy(password = String(decryptedBytes, StandardCharsets.UTF_8))
        }
        Log.d("GAG", "${decryptedPasswords}")
        return decryptedPasswords
    }
}