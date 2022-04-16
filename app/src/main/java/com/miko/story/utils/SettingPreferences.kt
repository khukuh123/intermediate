package com.miko.story.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val darkModeKey = booleanPreferencesKey(PreferenceUtil.DARK_MODE_KEY)

    fun getDarkModeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[darkModeKey] ?: false
        }
    }

    suspend fun setDarkModeSetting(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[darkModeKey] = isDarkMode
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        @JvmStatic
        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences =
            INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
    }
}