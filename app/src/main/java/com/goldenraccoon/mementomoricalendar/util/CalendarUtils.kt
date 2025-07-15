package com.goldenraccoon.mementomoricalendar.util

import android.icu.util.Calendar
import java.util.concurrent.TimeUnit

fun Calendar.getMillisPassedToday(): Long {
    val millisPassedToday = TimeUnit.HOURS.toMillis(get(Calendar.HOUR_OF_DAY).toLong()) +
            TimeUnit.MINUTES.toMillis(get(Calendar.MINUTE).toLong()) +
            TimeUnit.SECONDS.toMillis(get(Calendar.SECOND).toLong()) +
            get(Calendar.MILLISECOND)

    return millisPassedToday
}

fun Calendar.getMillisPassedThisWeek(): Long {
    val dayOfWeek = get(Calendar.DAY_OF_WEEK)
    // Calendar.DAY_OF_WEEK returns 1 for Sunday, 2 for Monday, ..., 7 for Saturday.
    // We want 0 for Monday, 1 for Tuesday, ..., 6 for Sunday.
    val dayOfWeekAdjusted = (dayOfWeek - 2 + 7) % 7
    val millisPassedThisWeek = TimeUnit.DAYS.toMillis(dayOfWeekAdjusted.toLong()) +
            getMillisPassedToday()

    return millisPassedThisWeek
}

fun Calendar.getMillisPassedThisMonth(): Long {
    val dayOfMonth = get(Calendar.DAY_OF_MONTH)
    val dayOfMonthAdjusted = dayOfMonth - 1     // Subtract 1 because dayOfMonth starts at 1

    val millisPassedThisMonth = TimeUnit.DAYS.toMillis(dayOfMonthAdjusted .toLong()) +
            getMillisPassedToday()

    return millisPassedThisMonth
}

fun Calendar.percentageOfDayPassed(): Double {
    val millisPassedToday = getMillisPassedToday()

    val percentage = millisPassedToday.toDouble() / TimeUnit.DAYS.toMillis(1L)
    return percentage
}

fun Calendar.percentageOfWeekPassed(): Double {
    val millisPassedThisWeek = getMillisPassedThisWeek()
    val millisInWeek = TimeUnit.DAYS.toMillis(7L)

    val percentage = millisPassedThisWeek.toDouble() / millisInWeek
    return percentage
}

fun Calendar.percentageOfMonthPassed(): Double {
    val millisPassedThisMonth = getMillisPassedThisMonth()
    val lastDayOfMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
    val millisInMonth = TimeUnit.DAYS.toMillis(lastDayOfMonth.toLong())

    val percentage = millisPassedThisMonth.toDouble() / millisInMonth
    return percentage
}

fun Calendar.yearsAlreadyLived(birthdayMillis: Long?): Int {
    if (birthdayMillis == null) {
        return 0
    }

    timeInMillis = birthdayMillis

    var months = 0
    while (timeInMillis < System.currentTimeMillis()) {
        add(Calendar.MONTH, 1)
        months++
    }
    months--

    val monthsLived = months.coerceAtLeast(0)
    val yearsLived = monthsLived / 12

    return yearsLived
}