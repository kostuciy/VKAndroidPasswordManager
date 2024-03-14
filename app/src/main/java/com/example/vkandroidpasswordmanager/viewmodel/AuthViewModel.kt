package com.example.vkandroidpasswordmanager.viewmodel

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import com.example.vkandroidpasswordmanager.model.helpers.BiometryHelper
import com.example.vkandroidpasswordmanager.model.helpers.PrefsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
//    private val repository: RepositoryImpl,
    private val biometryHelper: BiometryHelper,
    private val prefsHelper: PrefsHelper
) : ViewModel() {
    val biometryAvailability
        get() = biometryHelper.available

    fun checkBiometryCompatibility() =
        biometryHelper.compatibilityCheck()

    fun authenticate(biometricPrompt: BiometricPrompt) =
        biometryHelper.authenticate(biometricPrompt)

    fun getMasterPassword() = prefsHelper.password

    fun setMasterPassword(password: String) {
        prefsHelper.password = password
    }
}