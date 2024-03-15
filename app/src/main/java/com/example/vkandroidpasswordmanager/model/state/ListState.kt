package com.example.vkandroidpasswordmanager.model.state

data class ListState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false
)