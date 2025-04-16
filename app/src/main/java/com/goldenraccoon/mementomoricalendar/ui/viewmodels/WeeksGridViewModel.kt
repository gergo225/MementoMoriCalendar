package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WeeksGridViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    val elapsedWeeks = userSettingsRepository.birthdayMillis
        .map { birthday ->
            val currentMillis = Date().time
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
}
