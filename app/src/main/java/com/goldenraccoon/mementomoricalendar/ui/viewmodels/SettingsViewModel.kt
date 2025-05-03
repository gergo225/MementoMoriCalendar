package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.data.remainingWeeks
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import com.goldenraccoon.mementomoricalendar.util.LifeExpectancyValidator.isValidLifeExpectancy
import com.goldenraccoon.mementomoricalendar.widget.WidgetUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    @ApplicationContext private val applicationContext: Context
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

    @OptIn(DelicateCoroutinesApi::class)
    fun saveSettings() {
        viewModelScope.launch {
            updateLifeExpectancyIfNeeded()
            updateBirthdayIfNeeded()

            GlobalScope.launch(Dispatchers.IO) {
                updateWidget()
            }
        }
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

    private suspend fun updateWidget() {
        applicationContext.userSettingsDataStore.data.collect {
            val remainingWeeks = it.remainingWeeks() ?: return@collect
            WidgetUtils.updateRemainingWeeksWidgets(remainingWeeks, applicationContext)
        }
    }
}