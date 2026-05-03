package com.goldenraccoon.mementomoricalendar.data

import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import com.goldenraccoon.mementomoricalendar.util.Constants
import com.goldenraccoon.mementomoricalendar.util.getAlignedElapsedWeeks
import kotlin.math.roundToInt

fun UserSettings.remainingWeeks(): Int? {
    if (birthdayMillis == 0L || lifeExpectancyYears == 0) {
        return null
    }

    val elapsedWeeks = getAlignedElapsedWeeks(birthdayMillis)
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

fun UserSettings.weeksLived(): Int? {
    if (birthdayMillis == 0L || lifeExpectancyYears == 0) {
        return null
    }
    return getAlignedElapsedWeeks(birthdayMillis)
}

fun UserSettings.totalYears(): Int? {
    if (birthdayMillis == 0L || lifeExpectancyYears == 0) {
        return null
    }
    return lifeExpectancyYears
}