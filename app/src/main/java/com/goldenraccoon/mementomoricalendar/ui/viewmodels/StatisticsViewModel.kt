package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants
import com.goldenraccoon.mementomoricalendar.util.Constants.MILLIS_IN_DAY
import com.goldenraccoon.mementomoricalendar.util.Constants.WEEKS_IN_YEAR
import com.goldenraccoon.mementomoricalendar.util.getMillisPassedThisMonth
import com.goldenraccoon.mementomoricalendar.util.getMillisPassedThisWeek
import com.goldenraccoon.mementomoricalendar.util.getMillisPassedToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.roundToInt

enum class StatsRowType {
    LIVED, REMAINING
}

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    private val _statsRowType = MutableStateFlow(StatsRowType.REMAINING)
    val statsRowType = _statsRowType.asStateFlow()

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

    val statDays = userSettingsRepository.userSettingsFlow
        .combine(statsRowType) { settings, statsRowType ->
            val millisLived = System.currentTimeMillis() - settings.birthdayMillis

            val millis = when (statsRowType) {
                StatsRowType.LIVED -> millisLived
                StatsRowType.REMAINING -> {
                    val millisTotal = settings.lifeExpectancyYears * Constants.MILLIS_IN_YEAR
                    millisTotal - millisLived
                }
            }

            val days = millis.toDouble() / MILLIS_IN_DAY
            days.toInt().coerceAtLeast(0)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val statWeeks = statDays.map { it / 7 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val statMonths = userSettingsRepository.userSettingsFlow
        .combine(statsRowType) { settings, statsRowType ->
            val millisStart = when (statsRowType) {
                StatsRowType.LIVED -> settings.birthdayMillis
                StatsRowType.REMAINING -> System.currentTimeMillis()
            }
            val millisEnd = when (statsRowType) {
                StatsRowType.LIVED -> System.currentTimeMillis()
                StatsRowType.REMAINING -> settings.lifeExpectancyYears * Constants.MILLIS_IN_YEAR
            }

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millisStart

            var months = 0
            while (calendar.timeInMillis < millisEnd) {
                calendar.add(Calendar.MONTH, 1)
                months++
            }

            months.coerceAtLeast(0)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val statYears = statWeeks.map { it.toDouble() / WEEKS_IN_YEAR }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun onStatsRowTypeChanged(type: StatsRowType) {
        _statsRowType.update { type }
    }
}
