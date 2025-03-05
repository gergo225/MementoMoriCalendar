package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class BirthdayViewModel(private val userSettingsRepository: UserSettingsRepository) : ViewModel() {

    val birthdayMillis = userSettingsRepository.userSettingsFlow.map { it.birthdayMillis }
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = 0)

    fun setBirthday(birthdayMillis: Long) {
        runBlocking {
            userSettingsRepository.setBirthdayMillis(birthdayMillis)
        }
    }

}

class BirthdayViewModelFactory(private val repository: UserSettingsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BirthdayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BirthdayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
