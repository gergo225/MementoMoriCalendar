package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    sealed class ViewState {
        data object Birthday : ViewState()
        data object LifeExpectancy : ViewState()
    }

    var viewState: ViewState by mutableStateOf(ViewState.Birthday)
        private set

    var selectedDateMillis by mutableStateOf<Long?>(null)
        private set
    var lifeExpectancyInput by mutableStateOf(DEFAULT_LIFE_EXPECTANCY_YEARS.toString())
        private set

    val isContinueEnabled by derivedStateOf {
        selectedDateMillis != null
    }
    val isFinishEnabled by derivedStateOf {
        isValidLifeExpectancy(lifeExpectancyInput)
    }

    fun onDateSelected(dateMillis: Long?) {
        selectedDateMillis = dateMillis
    }

    fun onLifeExpectancyInputChange(newValue: String) {
        val hasInput = newValue.isNotEmpty()
        if (hasInput && !isValidLifeExpectancy(newValue)) {
            return
        }

        lifeExpectancyInput = newValue
    }

    fun setBirthday(birthdayMillis: Long) {
        runBlocking {
            userSettingsRepository.setBirthdayMillis(birthdayMillis)
        }
        viewState = ViewState.LifeExpectancy
    }

    fun setLifeExpectancy(yearsText: String) {
        val years = yearsText.toIntOrNull() ?: return
        runBlocking {
            userSettingsRepository.setLifeExpectancyYears(years)
        }
    }

    private fun isValidLifeExpectancy(value: String): Boolean {
        val intValue = value.toIntOrNull()
        val isGreaterThanZero = intValue != null && intValue > 0
        return isGreaterThanZero
    }
}
