package com.example.vkandroidpasswordmanager.viewmodel

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
    private val _state = MutableStateFlow(ListState())
    val state // TODO: show popup on error
        get() = _state

    private val _currentData = MutableStateFlow(CurrentDataState())
    val currentData: StateFlow<CurrentDataState>
        get() = _currentData

    val list = repository.list

    fun select(website: Website, withPasswords: Boolean = true) {
        _currentData.value = _currentData.value.copy(
            website = website
        )
        if (withPasswords) getPasswords(website.id)
    }

    fun select(password: Password) {
        _currentData.value = _currentData.value.copy(password = password)
    }

    fun selectPasswordToEmpty() {
        _currentData.value = _currentData.value.copy(password = Password())
    }

    fun selectToEmpty() {
        _currentData.value = _currentData.value.copy(
            website = Website(),
            passwords = emptyList(),
            password = Password()
        )
    }

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
        val passwords = _currentData.value.passwords
        _currentData.value = _currentData.value.copy(
            passwords = passwords.filter { it.id != id }
        )
    }

    fun save() {
        viewModelScope.launch {
            try {
                _state.value = ListState(loading = true)
                val websiteId = repository.save(
                    _currentData.value.website,
                    _currentData.value.passwords
                )
                val encryptedPasswords = encryptPasswords(
                    repository.getPasswords(websiteId)
                )
                repository.saveEncrypted(encryptedPasswords)

                _state.value = ListState()
            } catch (e: Exception) {
                _state.value = ListState(error = true)
            }
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
            try {
                _state.value = ListState(loading = true)
                val passwords = repository.getPasswords(websiteId)
                val decryptedPasswords = decryptPasswords(
                    passwords
                )
                _currentData.value = _currentData.value.copy(
                    passwords = decryptedPasswords
                )
                _state.value = ListState()
            } catch (e: Exception) {
                _state.value = ListState(error = true)
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun encryptPasswords(passwords: List<Password>): List<Password> {
        val ivPairs = (prefsHelper.getIvPairs() ?: emptyMap()).toMutableMap()

        if (passwords.isEmpty()) return emptyList()
        val encryptedPasswords = passwords.map { password ->
            val (iv, encryptedPassword) = encryptionHelper.encryptData(
                password.password.toByteArray()
            )
            ivPairs[password.id] = iv
            password.copy(password = Base64.encode(encryptedPassword))
        }
        prefsHelper.setIvPairs(ivPairs)

        return encryptedPasswords
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decryptPasswords(passwords: List<Password>): List<Password> {
        val ivPairs = prefsHelper.getIvPairs() ?: emptyMap()

        val decryptedPasswords = passwords.map { password ->
            ivPairs[password.id]?.let { iv ->
                val decryptedBytes = encryptionHelper.decryptData(
                    Base64.decode(password.password.toByteArray()), iv
                )

                password.copy(password = String(decryptedBytes, StandardCharsets.UTF_8))
            } ?: password
        }

        return decryptedPasswords
    }
}