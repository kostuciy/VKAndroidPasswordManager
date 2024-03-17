package com.example.vkandroidpasswordmanager.model.helpers

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.util.Currency
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionHelper @Inject constructor() {

    private val keyGenerator = KeyGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
    ).apply {
        init(
            KeyGenParameterSpec.Builder(
                "MyKeyAlias",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//                .setUserAuthenticationRequired(true) // 2 requires lock screen, invalidated if lock screen is disabled
//                .setRandomizedEncryptionRequired(true)
                .build()
        )
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getKey(): SecretKey =
        (keyStore.getEntry("MyKeyAlias", null) as? KeyStore.SecretKeyEntry)?.secretKey
            ?: keyGenerator.generateKey()

    private fun getEncryptCipher() = Cipher.getInstance("AES/GCM/NoPadding").apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    private fun getDecryptCipher(ivBytes: ByteArray): Cipher =
        Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, ivBytes))
        }


    //    encrypts given data and returns it with ivBytes for decryption
    fun encryptData(data: ByteArray): Pair<ByteArray, ByteArray> {
        val encryptCipher = getEncryptCipher()
        val encryptedData = encryptCipher.doFinal(data)

        return encryptCipher.iv to encryptedData
    }

    fun decryptData(data: ByteArray, ivBytes: ByteArray): ByteArray {
        return try {
            getDecryptCipher(ivBytes).doFinal(data)
        } catch (e: Exception) {
            data
        }
    }
}