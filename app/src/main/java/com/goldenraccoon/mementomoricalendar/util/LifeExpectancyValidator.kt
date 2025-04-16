package com.goldenraccoon.mementomoricalendar.util

object LifeExpectancyValidator {
    fun isValidLifeExpectancy(value: String): Boolean {
        val intValue = value.toIntOrNull()
        val isGreaterThanZero = intValue != null && intValue > 0
        return isGreaterThanZero
    }
}