package com.example.vkandroidpasswordmanager.viewmodel

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import com.example.vkandroidpasswordmanager.model.helpers.BiometryHelper
import com.example.vkandroidpasswordmanager.model.helpers.PrefsHelper
import com.example.vkandroidpasswordmanager.model.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
//    private val repository: RepositoryImpl,
    private val biometryHelper: BiometryHelper,
    private val prefsHelper: PrefsHelper
) : ViewModel() {
    private var _state= MutableStateFlow(AuthState())
    val state: StateFlow<AuthState>
        get() = _state

    private fun toDefault() {
        _state.value = AuthState()
    }

    fun toAuthenticated() {
        _state.value = AuthState(authenticated = true)
        toDefault()
    }

    val biometryAvailability
        get() = biometryHelper.available

    fun checkBiometryCompatibility() =
        biometryHelper.compatibilityCheck()

    fun authenticateBiometry(biometricPrompt: BiometricPrompt) =
        biometryHelper.authenticate(biometricPrompt)

    fun getMasterPassword() = prefsHelper.getPassword()

    fun hasMasterPassword() =
        getMasterPassword() != null

    fun setMasterPassword(password: String) {
        prefsHelper.setPassword(password)
        toAuthenticated()
    }
}