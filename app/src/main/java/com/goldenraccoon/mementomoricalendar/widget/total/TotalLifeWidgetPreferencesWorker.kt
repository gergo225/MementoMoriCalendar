package com.goldenraccoon.mementomoricalendar.widget.total

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.goldenraccoon.mementomoricalendar.data.percentageOfLifeLived
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_LIVED_KEY

class TotalLifeWidgetPreferencesWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            context.userSettingsDataStore.data.collect {
                val percentageLived = it.percentageOfLifeLived() ?: return@collect
                updateWidget(percentageLived)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(workDataOf("Failed to update 'TotalLife' widget" to e.localizedMessage))
        }
    }

    private suspend fun updateWidget(percentageLived: Int) {
        GlanceAppWidgetManager(context).getGlanceIds(TotalLifeWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { pref ->
                    pref[stringPreferencesKey(WIDGET_PERCENTAGE_LIVED_KEY)] =
                        percentageLived.toString()
                }
                TotalLifeWidget().updateAll(context)
            }
    }
}