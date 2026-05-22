package com.nivar.app.data.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val themePreference: Flow<String>
    suspend fun setThemePreference(theme: String)
}
