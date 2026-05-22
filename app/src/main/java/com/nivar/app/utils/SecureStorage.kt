package com.nivar.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * [CLASS] SecureStorage
 * [PURPOSE] Provides encrypted local storage for sensitive data.
 * [SECURITY] Uses Android Keystore System + AES256 encryption.
 * [BACKUP] Data saved here is encrypted, so even if backed up to cloud, it cannot be read without the device key.
 */
object SecureStorage {
    private const val PREFS_NAME = "nivar_secure_prefs"

    /**
     * [METHOD] Get Encrypted SharedPreferences
     * [RETURNS] SharedPreferences instance where keys and values are automatically encrypted/decrypted.
     */
    fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        // [KEY] Master Key stored in Android Hardware Keystore
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // [PREFS] Create/Open the encrypted file
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}