package com.example.vkandroidpasswordmanager.model.helpers

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.example.vkandroidpasswordmanager.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton      // TODO: check if needs to be singleton
class BiometryHelper @Inject constructor (
    @ApplicationContext private val context: Context
) {
    val available = android.os.Build.VERSION.SDK_INT > 29

    private val promptInfo: BiometricPrompt.PromptInfo by lazy {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.biometry_title))
            .setSubtitle(context.getString(R.string.biometry_subtitle))
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
    }

//    call before authentication fun
    fun compatibilityCheck(): Boolean =
        when (
            BiometricManager.from(context).canAuthenticate(
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> true

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_biometric_unavailable),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_biometric_enroll),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_biometrics_no_hw),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_biometric_security),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.no_supported_biometric_features_available),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_biometric_unknown),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            else -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_biometric_unknown),
                    Toast.LENGTH_LONG
                ).show()
                false
            }

        }

    fun authenticate(biometricPrompt: BiometricPrompt) {
        if (!available) return
        biometricPrompt.authenticate(promptInfo)
    }
}