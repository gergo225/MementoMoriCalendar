package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val birthdayMillis = userSettingsRepository.userSettingsFlow.map { it.birthdayMillis }
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = 0)

    fun setBirthday(birthdayMillis: Long) {
        runBlocking {
            userSettingsRepository.setBirthdayMillis(birthdayMillis)
        }
    }

}
