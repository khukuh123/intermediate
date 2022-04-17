package com.miko.story.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class SettingPreferences(private val dataStore: DataStore<Preferences>) {

    private val tokenKey = stringPreferencesKey(PreferenceUtil.TOKEN_KEY)

    suspend fun getToken(): Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[tokenKey] ?: ""
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    suspend fun clearToken(){
        dataStore.edit { preferences ->
            preferences[tokenKey] = ""
        }
    }
}