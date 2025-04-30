package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import com.goldenraccoon.mementomoricalendar.widget.WidgetPreferencesWorker
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
        // TODO: run periodically to make sure remaining weeks is up to date (make sure not to create multiple workers, only 1 in total)
        val work = PeriodicWorkRequestBuilder<WidgetPreferencesWorker>(10, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(work)
    }
}
