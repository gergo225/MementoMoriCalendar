package com.goldenraccoon.mementomoricalendar.data

import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import com.goldenraccoon.mementomoricalendar.util.Constants
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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

fun UserSettings.percentageOfLifeLived(): Int? {
    if (birthdayMillis == 0L || lifeExpectancyYears == 0) {
        return null
    }

    val millisLived = System.currentTimeMillis() - birthdayMillis
    val millisTotal = lifeExpectancyYears.toLong() * Constants.MILLIS_IN_YEAR

    val percentage = millisLived.toDouble() / millisTotal
    return (percentage * 100).roundToInt()
}