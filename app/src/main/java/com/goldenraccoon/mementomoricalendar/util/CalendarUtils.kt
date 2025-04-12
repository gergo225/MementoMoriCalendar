package com.goldenraccoon.mementomoricalendar.util

import android.icu.util.Calendar
import com.goldenraccoon.mementomoricalendar.util.Constants.MILLIS_IN_DAY
import com.goldenraccoon.mementomoricalendar.util.Constants.MILLIS_IN_HOUR
import com.goldenraccoon.mementomoricalendar.util.Constants.MILLIS_IN_MINUTE
import com.goldenraccoon.mementomoricalendar.util.Constants.MILLIS_IN_SECOND

fun Calendar.getMillisPassedToday(): Long {
    val millisPassedToday = get(Calendar.HOUR_OF_DAY) * MILLIS_IN_HOUR +
            get(Calendar.MINUTE) * MILLIS_IN_MINUTE +
            get(Calendar.SECOND) * MILLIS_IN_SECOND +
            get(Calendar.MILLISECOND)

    return millisPassedToday
}

fun Calendar.getMillisPassedThisWeek(): Long {
    val dayOfWeek = get(Calendar.DAY_OF_WEEK)
    // Calendar.DAY_OF_WEEK returns 1 for Sunday, 2 for Monday, ..., 7 for Saturday.
    // We want 0 for Monday, 1 for Tuesday, ..., 6 for Sunday.
    val dayOfWeekAdjusted = (dayOfWeek - 2 + 7) % 7
    val millisPassedThisWeek = dayOfWeekAdjusted * MILLIS_IN_DAY +
            getMillisPassedToday()

    return millisPassedThisWeek
}

fun Calendar.getMillisPassedThisMonth(): Long {
    val dayOfMonth = get(Calendar.DAY_OF_MONTH)
    val dayOfMonthAdjusted = dayOfMonth - 1     // Subtract 1 because dayOfMonth starts at 1

    val millisPassedThisMonth = dayOfMonthAdjusted * MILLIS_IN_DAY +
            getMillisPassedToday()

    return millisPassedThisMonth
}
