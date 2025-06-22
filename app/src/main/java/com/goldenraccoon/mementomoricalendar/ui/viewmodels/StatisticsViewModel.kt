package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants
import com.goldenraccoon.mementomoricalendar.util.Constants.WEEKS_IN_YEAR
import com.goldenraccoon.mementomoricalendar.util.percentageOfDayPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfMonthPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfWeekPassed
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
import java.util.concurrent.TimeUnit
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
            val percentage = calendar.percentageOfDayPassed()
            emit((percentage * 100).roundToInt())
            delay(1000)
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val percentageOfWeekPassed = flow {
        while (true) {
            val calendar = Calendar.getInstance()
            val percentage = calendar.percentageOfWeekPassed()
            emit((percentage * 100).roundToInt())
            delay(1000)
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val percentageOfMonthPassed = flow {
        while (true) {
            val calendar = Calendar.getInstance()
            val percentage = calendar.percentageOfMonthPassed()
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

            val days = millis.toDouble() / TimeUnit.DAYS.toMillis(1L)
            days.toInt().coerceAtLeast(0)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val statWeeks = statDays
        .combine(statsRowType) { days, statsRowType ->
            days / 7 + if (statsRowType == StatsRowType.REMAINING) 1 else 0
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val statMonths = userSettingsRepository.userSettingsFlow
        .combine(statsRowType) { settings, statsRowType ->
            val millisStart = when (statsRowType) {
                StatsRowType.LIVED -> settings.birthdayMillis
                StatsRowType.REMAINING -> System.currentTimeMillis()
            }
            val millisEnd = when (statsRowType) {
                StatsRowType.LIVED -> System.currentTimeMillis()
                StatsRowType.REMAINING -> settings.birthdayMillis + settings.lifeExpectancyYears * Constants.MILLIS_IN_YEAR
            }

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millisStart

            var months = 0
            while (calendar.timeInMillis < millisEnd) {
                calendar.add(Calendar.MONTH, 1)
                months++
            }
            months--

            months.coerceAtLeast(0)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val statYears = statWeeks.map { it.toDouble() / WEEKS_IN_YEAR }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun onStatsRowTypeChanged(type: StatsRowType) {
        _statsRowType.update { type }
    }
}
