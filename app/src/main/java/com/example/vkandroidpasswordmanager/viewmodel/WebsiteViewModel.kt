package com.example.vkandroidpasswordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website
import com.example.vkandroidpasswordmanager.model.repo.Repository
import com.example.vkandroidpasswordmanager.model.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebsiteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private var _state = MutableStateFlow(ListState())
    val state // TODO: show popup on error
        get() = _state

    val websiteList = repository.list

    private var _currentWebsite = Website()
    val currentWebsite
        get() = _currentWebsite

    private var _currentPassword = Password()
    val currentPassword
        get() = _currentPassword

    fun select(password: Password) {
        _currentPassword = password
    }

    fun select(website: Website, withPasswords: Boolean = true) {
        _currentWebsite = website
        if (withPasswords) getPasswords(website.id)
    }

    fun selectPasswordToEmpty() {
        _currentPassword = Password()
    }

    fun selectToEmpty() {
        _currentWebsite = Website()
        _currentWebsitePasswords.value = emptyList()
        selectPasswordToEmpty()
    }

    private var _currentWebsitePasswords =
        MutableStateFlow<List<Password>>(emptyList())

    val currentWebsitePasswords
        get() = _currentWebsitePasswords

    fun addPassword() {
        val passwords = _currentWebsitePasswords.value
        _currentWebsitePasswords.value =
            if (currentPassword.id in passwords.map { it.id })
                passwords.map { if (it.id == currentPassword.id) currentPassword else it }
            else passwords + currentPassword
    }

    fun removePassword(id: Long) {
        val passwords = _currentWebsitePasswords.value
        _currentWebsitePasswords.value = passwords.filter { it.id != id }
    }

    fun save() {
        viewModelScope.launch {
            try {
                _state.value = ListState(loading = true)
                repository.save(_currentWebsite, _currentWebsitePasswords.value)
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

    fun delete(idList: List<Long>) {
        viewModelScope.launch {
            try {
                _state.value = ListState(loading = true)
                repository.deleteWebsites(idList)
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
                _currentWebsitePasswords.value =
                    repository.getPasswords(websiteId)
                _state.value = ListState()
            } catch (e: Exception) {
                _state.value = ListState(error = true)
            }
        }
    }
}