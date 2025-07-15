package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.data.percentageOfLifeLived
import com.goldenraccoon.mementomoricalendar.data.remainingWeeks
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import com.goldenraccoon.mementomoricalendar.util.LifeExpectancyValidator.isValidLifeExpectancyAndBirthday
import com.goldenraccoon.mementomoricalendar.util.LifeExpectancyValidator.isValidLifeExpectancyInput
import com.goldenraccoon.mementomoricalendar.util.LifeExpectancyValidator.minLifeExpectancyNeeded
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
        val lifeExpectancyChanged = isLifeExpectancyInputChanged()
        val birthdayChanged = isBirthdayMillisChanged()

        val conditions = mutableListOf<Boolean>()

        if (lifeExpectancyChanged) {
            val isLifeExpectancyInputValid = isValidLifeExpectancyInput(lifeExpectancyInput)
            conditions.add(isLifeExpectancyInputValid)
        }
        if (birthdayChanged) {
            val isBirthdayInputValid = isValidBirthday(birthdayMillisInput)
            conditions.add(isBirthdayInputValid)
        }

        if (lifeExpectancyChanged || birthdayChanged) {
            val lifeExpectancy = lifeExpectancyInput.toIntOrNull() ?: lifeExpectancyYears.value
            val birthdayMillis = birthdayMillisInput ?: initialBirthdayMillis.value
            val isValidLifeExpectancyAndBirthday = isValidLifeExpectancyAndBirthday(lifeExpectancy, birthdayMillis)

            conditions.add(isValidLifeExpectancyAndBirthday)
        }

        conditions.isNotEmpty() && conditions.all { it }
    }

    var lifeExpectancyInput by mutableStateOf("")
        private set
    var birthdayMillisInput by mutableStateOf<Long?>(null)
        private set

    var lifeExpectancyWarning by mutableStateOf<String?>(null)
        private set
    var birthdayWarning by mutableStateOf<String?>(null)
        private set

    fun onLifeExpectancyInputChange(newValue: String) {
        val hasInput = newValue.isNotEmpty()
        if (hasInput && !isValidLifeExpectancyInput(newValue)) {
            return
        }

        val isLifeExpectancyTooSmall = !isValidLifeExpectancyAndBirthday(
            newValue.toIntOrNull(),
            birthdayMillisInput ?: initialBirthdayMillis.value
        )
        if (isLifeExpectancyTooSmall) {
            showLifeExpectancyTooSmallError()
        } else {
            hideLifeExpectancyTooSmallError()
            hideBirthdayTooLateError()
        }

        lifeExpectancyInput = newValue
    }

    fun onBirthdayInputChange(newValue: Long?) {
        if (!isValidBirthday(newValue)) {
            return
        }

        val isBirthdayTooLate = !isValidLifeExpectancyAndBirthday(
            lifeExpectancyInput.toIntOrNull(),
            newValue
        )

        birthdayMillisInput = newValue

        if (isBirthdayTooLate) {
            showBirthdayTooLateError()
        } else {
            hideBirthdayTooLateError()
            hideLifeExpectancyTooSmallError()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveSettings() {
        viewModelScope.launch {
            updateLifeExpectancyIfNeeded()
            updateBirthdayIfNeeded()

            GlobalScope.launch(Dispatchers.IO) {
                updateWidgets()
            }
        }
    }

    private fun isLifeExpectancyInputChanged(): Boolean {
        val lifeExpectancy = lifeExpectancyInput.toIntOrNull() ?: return false
        return lifeExpectancy != lifeExpectancyYears.value
    }

    private fun isBirthdayMillisChanged(): Boolean {
        val birthdayMillis = birthdayMillisInput ?: return false
        return birthdayMillis != initialBirthdayMillis.value
    }

    private fun isValidBirthday(value: Long?): Boolean {
        val isPastDate = value != null && value < System.currentTimeMillis()
        val isExistingDate = (value ?: 0) > 0
        return isPastDate && isExistingDate
    }

    private fun showLifeExpectancyTooSmallError() {
        val birthdayMillis = birthdayMillisInput ?: initialBirthdayMillis.value
        val minimumLifeExpectancy = minLifeExpectancyNeeded(birthdayMillis)
        lifeExpectancyWarning = "Must be $minimumLifeExpectancy or greater."
    }

    private fun hideLifeExpectancyTooSmallError() {
        if (lifeExpectancyWarning == null) {
            return
        }
        lifeExpectancyWarning = null
    }

    private fun showBirthdayTooLateError() {
        val birthdayMillis = birthdayMillisInput ?: return
        val minimumLifeExpectancy = minLifeExpectancyNeeded(birthdayMillis)
        birthdayWarning = "Life expectancy must be $minimumLifeExpectancy or greater for this birthdate."
    }

    private fun hideBirthdayTooLateError() {
        if (birthdayWarning == null) {
            return
        }
        birthdayWarning = null
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

    private suspend fun updateWidgets() {
        applicationContext.userSettingsDataStore.data.collect {
            val remainingWeeks = it.remainingWeeks()
            if (remainingWeeks != null) {
                WidgetUtils.updateRemainingWeeksWidgets(remainingWeeks, applicationContext)
            }

            val percentageLived = it.percentageOfLifeLived() ?: 0
            WidgetUtils.updateTotalLifeWidget(percentageLived, applicationContext)
        }
    }
}