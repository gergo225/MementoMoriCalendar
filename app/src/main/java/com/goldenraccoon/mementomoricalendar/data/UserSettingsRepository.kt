package com.goldenraccoon.mementomoricalendar.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepository @Inject constructor(
    private val userSettingsStore: DataStore<UserSettings>
) {
    private val TAG = "UserSettingsRepo"

    val userSettingsFlow: Flow<UserSettings> = userSettingsStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading user settings.", exception)
            } else {
                throw exception
            }
        }

    val isBirthdaySet: Flow<Boolean> = userSettingsFlow.map { it.birthdayMillis != 0L }

    val birthdayMillis: Flow<Long> = userSettingsFlow
        .map {
            it.birthdayMillis
        }

    suspend fun setBirthdayMillis(birthdayMillis: Long) {
        userSettingsStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setBirthdayMillis(birthdayMillis)
                .build()
        }
    }

    val lifeExpectancyYears: Flow<Int> = userSettingsFlow
        .map {
            if (it.lifeExpectancyYears == 0) {
                DEFAULT_LIFE_EXPECTANCY_YEARS
            } else {
                it.lifeExpectancyYears
            }
        }

    suspend fun setLifeExpectancyYears(years: Int) {
        userSettingsStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setLifeExpectancyYears(years)
                .build()
        }
    }
}