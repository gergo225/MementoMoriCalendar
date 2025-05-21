package com.goldenraccoon.mementomoricalendar.util

object Constants {
    const val WEEKS_IN_YEAR = 52
    const val WEEKS_IN_QUARTER = 13
    const val DEFAULT_LIFE_EXPECTANCY_YEARS = 80

    private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
    const val MILLIS_IN_YEAR = WEEKS_IN_YEAR * 7 * MILLIS_IN_DAY
}

object DataStoreConstants {
    const val WIDGET_REMAINING_WEEKS_KEY = "remaining_weeks_key"
    const val WIDGET_PERCENTAGE_LIVED_KEY = "percentage_lived_key"
    const val WIDGET_PERCENTAGE_OF_DAY_KEY = "percentage_of_day_key"
    const val WIDGET_PERCENTAGE_OF_WEEK_KEY = "percentage_of_week_key"
    const val WIDGET_PERCENTAGE_OF_MONTH_KEY = "percentage_of_month_key"
}