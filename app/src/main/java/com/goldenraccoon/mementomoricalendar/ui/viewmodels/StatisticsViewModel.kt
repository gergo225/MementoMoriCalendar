package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants
import com.goldenraccoon.mementomoricalendar.util.Constants.MILLIS_IN_DAY
import com.goldenraccoon.mementomoricalendar.util.getMillisPassedThisMonth
import com.goldenraccoon.mementomoricalendar.util.getMillisPassedThisWeek
import com.goldenraccoon.mementomoricalendar.util.getMillisPassedToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    val percentageLived = userSettingsRepository.userSettingsFlow
        .map {
            val millisLived = System.currentTimeMillis() - it.birthdayMillis
            val millisTotal = it.lifeExpectancyYears.toLong() * Constants.MILLIS_IN_YEAR

            val percentage = millisLived.toDouble() / millisTotal
            (percentage * 100).roundToInt()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val percentageOfDayPassed = flow {
        while (true) {
            val calendar = Calendar.getInstance()
            val millisPassedToday = calendar.getMillisPassedToday()

            val percentage = millisPassedToday.toDouble() / MILLIS_IN_DAY
            emit((percentage * 100).roundToInt())
            delay(1000)
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val percentageOfWeekPassed = flow {
        while (true) {
            val calendar = Calendar.getInstance()
            val millisPassedThisWeek = calendar.getMillisPassedThisWeek()
            val millisInWeek = 7 * MILLIS_IN_DAY

            val percentage = millisPassedThisWeek.toDouble() / millisInWeek
            emit((percentage * 100).roundToInt())
            delay(1000)
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val percentageOfMonthPassed = flow {
        while (true) {
            val calendar = Calendar.getInstance()
            val millisPassedThisMonth = calendar.getMillisPassedThisMonth()
            val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val millisInMonth = lastDayOfMonth * MILLIS_IN_DAY

            val percentage = millisPassedThisMonth.toDouble() / millisInMonth
            emit((percentage * 100).roundToInt())
            delay(1000)
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
}
