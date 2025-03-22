package com.goldenraccoon.mementomoricalendar.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    fun setBirthday(birthdayMillis: Long) {
        runBlocking {
            userSettingsRepository.setBirthdayMillis(birthdayMillis)
        }
    }

}
