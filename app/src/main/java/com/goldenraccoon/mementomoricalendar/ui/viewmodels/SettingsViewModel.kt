package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.compose.runtime.derivedStateOf
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
    var initialBirthdayMillis = userSettingsRepository.birthdayMillis
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val isSaveEnabled by derivedStateOf {
        val lifeExpectancyChanged = lifeExpectancyInput.toIntOrNull() != lifeExpectancyYears.value
        val birthdayChanged = birthdayMillisInput != initialBirthdayMillis.value

        (isValidLifeExpectancy(lifeExpectancyInput) && lifeExpectancyChanged)
                || (isValidBirthday(birthdayMillisInput) && birthdayChanged)
    }

    var lifeExpectancyInput by mutableStateOf("")
        private set
    var birthdayMillisInput by mutableStateOf<Long?>(null)
        private set

    fun onLifeExpectancyInputChange(newValue: String) {
        val hasInput = newValue.isNotEmpty()
        if (hasInput && !isValidLifeExpectancy(newValue)) {
            return
        }

        lifeExpectancyInput = newValue
    }

    fun onBirthdayInputChange(newValue: Long?) {
        if (!isValidBirthday(newValue)) {
            return
        }

        birthdayMillisInput = newValue
    }

    fun saveSettings() {
        viewModelScope.launch {
            updateLifeExpectancyIfNeeded()
            updateBirthdayIfNeeded()
        }
    }

    private fun isValidLifeExpectancy(value: String): Boolean {
        val intValue = value.toIntOrNull()
        val isGreaterThanZero = intValue != null && intValue > 0
        return isGreaterThanZero
    }

    private fun isValidBirthday(value: Long?): Boolean {
        val isPastDate = value != null && value < System.currentTimeMillis()
        val isExistingDate = (value ?: 0) > 0
        return isPastDate && isExistingDate
    }

    private suspend fun updateLifeExpectancyIfNeeded() {
        if (lifeExpectancyInput.toIntOrNull() == lifeExpectancyYears.value) { return }

        lifeExpectancyInput.toIntOrNull()?.let {
            userSettingsRepository.setLifeExpectancyYears(it)
        }
    }

    private suspend fun updateBirthdayIfNeeded() {
        if (birthdayMillisInput == initialBirthdayMillis.value) { return }

        birthdayMillisInput?.let {
            userSettingsRepository.setBirthdayMillis(it)
        }
    }
}