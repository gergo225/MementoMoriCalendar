package com.goldenraccoon.mementomoricalendar.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class UserSettingsRepository(private val userSettingsStore: DataStore<UserSettings>) {
    private val TAG = "UserSettingsRepo"

    val userSettingsFlow: Flow<UserSettings> = userSettingsStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading user settings.", exception)
            } else {
                throw exception
            }
        }

    suspend fun setBirthdayMillis(birthdayMillis: Long) {
        userSettingsStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setBirthdayMillis(birthdayMillis)
                .build()
        }
    }
}