package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import com.goldenraccoon.mementomoricalendar.widget.RemainingWeeksWidgetPreferencesWorker
import com.goldenraccoon.mementomoricalendar.widget.TotalLifeWidgetPreferencesWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WeeksGridViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository,
    @ApplicationContext application: Context
) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)

    init {
        setWork()
    }

    val elapsedWeeks = userSettingsRepository.birthdayMillis
        .map { birthday ->
            val currentMillis = System.currentTimeMillis()
            val elapsedMillis = currentMillis - birthday

            val elapsedDays = TimeUnit.MILLISECONDS.toDays(elapsedMillis)
            val elapsedWeeks = elapsedDays / 7

            elapsedWeeks.toInt()
        }
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = 0)

    val lifeExpectancyYears = userSettingsRepository.lifeExpectancyYears
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = DEFAULT_LIFE_EXPECTANCY_YEARS)

    val shouldShowSetupPage = userSettingsRepository.isBirthdaySet
        .combine(userSettingsRepository.isLifeExpectancySet) { isBirthdaySet, isLifeExpectancySet ->
            !isBirthdaySet || !isLifeExpectancySet
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private fun setWork() {
        val remainingWeeksWork = PeriodicWorkRequestBuilder<RemainingWeeksWidgetPreferencesWorker>(10, TimeUnit.SECONDS)
            .build()
        val totalLifeWork = PeriodicWorkRequestBuilder<TotalLifeWidgetPreferencesWorker>(1, TimeUnit.DAYS)
            .build()
        workManager.enqueueUniquePeriodicWork("updateRemainingWeeksWidgetPreferences", ExistingPeriodicWorkPolicy.KEEP, remainingWeeksWork)
        workManager.enqueueUniquePeriodicWork("updateTotalLifeWidgetPreferences", ExistingPeriodicWorkPolicy.KEEP, totalLifeWork)
    }
}
