package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository
): ViewModel() {
    val percentageLived = userSettingsRepository.userSettingsFlow
        .map {
            val millisLived = System.currentTimeMillis() - it.birthdayMillis
            val millisTotal = it.lifeExpectancyYears.toLong() * Constants.MILLIS_IN_A_YEAR

            val percentage = millisLived.toDouble() / millisTotal
            (percentage * 100).roundToInt()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
}
