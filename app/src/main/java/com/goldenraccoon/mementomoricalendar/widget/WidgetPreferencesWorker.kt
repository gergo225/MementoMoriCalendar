package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.goldenraccoon.mementomoricalendar.data.remainingWeeks
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_REMAINING_WEEKS_KEY

class WidgetPreferencesWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            context.userSettingsDataStore.data.collect {
                val remainingWeeks = it.remainingWeeks() ?: return@collect
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
                    pref[stringPreferencesKey(WIDGET_REMAINING_WEEKS_KEY)] =
                        remainingWeeks.toString()
                }
                MementoMoriAppWidget().updateAll(context)
            }
    }
}
