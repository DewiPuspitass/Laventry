package com.dewipuspitasari0020.laventry.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dewipuspitasari0020.laventry.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserDataStore (private val context: Context){
    companion object {
        val USER_NAME = stringPreferencesKey("name")
        val USER_EMAIL = stringPreferencesKey("email")
        val USER_PHOTO = stringPreferencesKey("photoUrl")
    }

    val userFlow: Flow<User> = context.dataStore.data.map { prefereces ->
        User(
            name = prefereces[USER_NAME] ?: "",
            email = prefereces[USER_EMAIL] ?: "",
            photoUrl = prefereces[USER_PHOTO] ?: ""
        )
    }

    suspend fun saveData(user:User) {
        context.dataStore.edit{ preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            preferences[USER_PHOTO] = user.photoUrl
        }
    }
}