package com.goldenraccoon.mementomoricalendar.data

import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import com.goldenraccoon.mementomoricalendar.util.Constants
import java.util.concurrent.TimeUnit

fun UserSettings.remainingWeeks(): Int? {
    if (birthdayMillis == 0L || lifeExpectancyYears == 0) {
        return null
    }

    val currentMillis = System.currentTimeMillis()
    val elapsedMillis = currentMillis - birthdayMillis

    val elapsedDays = TimeUnit.MILLISECONDS.toDays(elapsedMillis)
    val elapsedWeeks = (elapsedDays / 7).toInt()

    val totalWeeks = lifeExpectancyYears * Constants.WEEKS_IN_YEAR
    return totalWeeks - elapsedWeeks
}
