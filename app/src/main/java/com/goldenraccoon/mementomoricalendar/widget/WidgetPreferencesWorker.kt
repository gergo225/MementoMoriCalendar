package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import com.goldenraccoon.mementomoricalendar.util.Constants
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_BIRTHDAY_MILLIS_KEY
import java.util.concurrent.TimeUnit

class WidgetPreferencesWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            context.userSettingsDataStore.data.collect {
                val remainingWeeks = it.remainingWeeks()
                updateWidget(remainingWeeks)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(workDataOf("Failed to update widget" to e.localizedMessage))
        }
    }

    private suspend fun updateWidget(remainingWeeks: Int) {
        GlanceAppWidgetManager(context).getGlanceIds(MementoMoriAppWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { pref ->
                    pref[stringPreferencesKey(WIDGET_BIRTHDAY_MILLIS_KEY)] =
                        remainingWeeks.toString()
                }
                MementoMoriAppWidget().updateAll(context)
            }
    }
}

private fun UserSettings.remainingWeeks(): Int {
    val currentMillis = System.currentTimeMillis()
    val elapsedMillis = currentMillis - birthdayMillis

    val elapsedDays = TimeUnit.MILLISECONDS.toDays(elapsedMillis)
    val elapsedWeeks = (elapsedDays / 7).toInt()

    val totalWeeks = lifeExpectancyYears * Constants.WEEKS_IN_YEAR
    return totalWeeks - elapsedWeeks
}
