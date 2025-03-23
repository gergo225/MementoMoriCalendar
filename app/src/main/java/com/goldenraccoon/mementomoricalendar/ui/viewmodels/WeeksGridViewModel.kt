package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WeeksGridViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    val elapsedWeeks = userSettingsRepository.userSettingsFlow
        .map {
            val currentMillis = Date().time
            val birthday = it.birthdayMillis
            val elapsedMillis = currentMillis - birthday

            val elapsedDays = TimeUnit.MILLISECONDS.toDays(elapsedMillis)
            val elapsedWeeks = elapsedDays / 7

            elapsedWeeks.toInt()
        }
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = 0)

    val shouldShowBirthdayPage = userSettingsRepository.isBirthdaySet.map { !it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
