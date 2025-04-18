package com.goldenraccoon.mementomoricalendar.util

import android.icu.util.Calendar
import java.util.concurrent.TimeUnit

fun Calendar.getMillisPassedToday(): Long {
    val millisPassedToday = TimeUnit.HOURS.toMillis(Calendar.HOUR_OF_DAY.toLong()) +
            TimeUnit.MINUTES.toMillis(Calendar.MINUTE.toLong()) +
            TimeUnit.SECONDS.toMillis(Calendar.SECOND.toLong()) +
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
