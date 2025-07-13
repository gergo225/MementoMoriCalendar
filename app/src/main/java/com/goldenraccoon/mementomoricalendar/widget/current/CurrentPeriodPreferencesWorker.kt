package com.goldenraccoon.mementomoricalendar.widget.current

import android.content.Context
import android.icu.util.Calendar
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.percentageOfDayPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfMonthPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfWeekPassed
import com.goldenraccoon.mementomoricalendar.widget.WidgetUtils
import kotlin.math.roundToInt

class CurrentPeriodPreferencesWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            context.userSettingsDataStore.data.collect {
                val calendar = Calendar.getInstance()
                val dayPercentage = (calendar.percentageOfDayPassed() * 100).roundToInt()
                val weekPercentage = (calendar.percentageOfWeekPassed() * 100).roundToInt()
                val monthPercentage = (calendar.percentageOfMonthPassed() * 100).roundToInt()

                WidgetUtils.updateCurrentPeriodWidget(
                    dayPercentage = dayPercentage,
                    weekPercentage = weekPercentage,
                    monthPercentage = monthPercentage,
                    context = context
                )
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(workDataOf("Failed to update 'CurrentPeriod' widget" to e.localizedMessage))
        }
    }
}