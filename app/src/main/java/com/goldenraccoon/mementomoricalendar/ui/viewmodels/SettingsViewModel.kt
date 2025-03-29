package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    var lifeExpectancyYears = userSettingsRepository.lifeExpectancyYears
        .stateIn(viewModelScope, SharingStarted.Eagerly, DEFAULT_LIFE_EXPECTANCY_YEARS)

    var lifeExpectancyInput by mutableStateOf("")
        private set

    fun onLifeExpectancyInputChange(newValue: String) {
        val hasInput = newValue.isNotEmpty()
        if (hasInput && !isValidLifeExpectancy(newValue)) {
            return
        }

        lifeExpectancyInput = newValue
    }

    fun saveSettings() {
        updateLifeExpectancy()
    }

    private fun isValidLifeExpectancy(value: String): Boolean {
        val intValue = value.toIntOrNull()
        val isGreaterThanZero = intValue != null && intValue > 0
        return isGreaterThanZero
    }

    private fun updateLifeExpectancy() {
        lifeExpectancyInput.toIntOrNull()?.let {
            viewModelScope.launch {
                userSettingsRepository.setLifeExpectancyYears(it)
            }
        }
    }
}