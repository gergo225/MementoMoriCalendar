package com.goldenraccoon.mementomoricalendar.widget.grid

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.goldenraccoon.mementomoricalendar.data.totalYears
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.data.weeksLived
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_TOTAL_YEARS_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_WEEKS_LIVED_KEY

class WeeksGridWidgetPreferencesWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            context.userSettingsDataStore.data.collect {
                val weeksLived = it.weeksLived() ?: return@collect
                val totalYears = it.totalYears() ?: return@collect
                updateWidget(weeksLived, totalYears)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(workDataOf("Failed to update 'WeeksGrid' widget" to e.localizedMessage))
        }
    }

    private suspend fun updateWidget(weeksLived: Int, totalYears: Int) {
        GlanceAppWidgetManager(context).getGlanceIds(WeeksGridWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { pref ->
                    pref[stringPreferencesKey(WIDGET_WEEKS_LIVED_KEY)] = weeksLived.toString()
                    pref[stringPreferencesKey(WIDGET_TOTAL_YEARS_KEY)] = totalYears.toString()
                }
                WeeksGridWidget().updateAll(context)
            }
    }
}
