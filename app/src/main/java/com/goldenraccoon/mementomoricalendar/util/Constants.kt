package com.goldenraccoon.mementomoricalendar.util

object Constants {
    const val WEEKS_IN_A_YEAR = 52
    const val WEEKS_IN_A_QUARTER = 13
    const val DEFAULT_LIFE_EXPECTANCY_YEARS = 80
    const val MILLIS_IN_A_YEAR: Long = WEEKS_IN_A_YEAR.toLong() * 7 * 24 * 60 * 60 * 1000
}