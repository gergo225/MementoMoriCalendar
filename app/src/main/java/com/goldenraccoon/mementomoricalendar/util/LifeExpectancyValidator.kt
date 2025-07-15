package com.goldenraccoon.mementomoricalendar.util

import android.icu.util.Calendar

object LifeExpectancyValidator {
    fun isValidLifeExpectancyInput(value: String): Boolean {
        val intValue = value.toIntOrNull() ?: return false
        val isGreaterThanZero = intValue > 0
        return isGreaterThanZero
    }

    fun isValidLifeExpectancyAndBirthday(lifeExpectancy: Int?, birthdayMillis: Long?): Boolean {
        val yearsAlreadyLived = Calendar.getInstance().yearsAlreadyLived(birthdayMillis)

        if (lifeExpectancy == null) {
            return false
        }

        return yearsAlreadyLived < lifeExpectancy
    }

    fun minLifeExpectancyNeeded(birthdayMillis: Long): Int {
        val yearsAlreadyLived = Calendar.getInstance().yearsAlreadyLived(birthdayMillis)

        return yearsAlreadyLived + 1
    }
}