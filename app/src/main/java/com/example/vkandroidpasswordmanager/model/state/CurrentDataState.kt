package com.example.vkandroidpasswordmanager.model.state

import com.example.vkandroidpasswordmanager.model.dto.Password
import com.example.vkandroidpasswordmanager.model.dto.Website

data class CurrentDataState(
    val website: Website = Website(),
    val passwords: List<Password> = emptyList(),
    val password: Password = Password()
)