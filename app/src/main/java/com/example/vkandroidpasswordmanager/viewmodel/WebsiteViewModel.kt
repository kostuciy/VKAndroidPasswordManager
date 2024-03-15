package com.example.vkandroidpasswordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val list = repository.list

    private var _state = MutableStateFlow(ListState())
    val state
        get() = _state

    private var _currentWebsite = Website()
    val currentWebsite
        get() = _currentWebsite

    fun select(website: Website) {
        _currentWebsite = website
    }
    fun save() {
        viewModelScope.launch {
            try {
                _state.value = ListState(loading = true)
                repository.save(_currentWebsite)
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
                repository.delete(id)
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
                repository.delete(idList)
                _state.value = ListState()
            } catch (e: Exception) {
                _state.value = ListState(error = true)
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            try {
                _state.value = ListState(loading = true)
                repository.deleteAll()
                _state.value = ListState()
            } catch (e: Exception) {
                _state.value = ListState(error = true)
            }
        }
    }



}