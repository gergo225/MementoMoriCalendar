package com.goldenraccoon.mementomoricalendar.util

object Constants {
    const val WEEKS_IN_YEAR = 52
    const val WEEKS_IN_QUARTER = 13
    const val DEFAULT_LIFE_EXPECTANCY_YEARS = 80

    private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
    const val MILLIS_IN_YEAR = WEEKS_IN_YEAR * 7 * MILLIS_IN_DAY
}