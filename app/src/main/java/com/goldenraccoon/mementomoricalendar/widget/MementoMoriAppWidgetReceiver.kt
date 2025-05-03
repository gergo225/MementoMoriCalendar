package com.goldenraccoon.mementomoricalendar.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.goldenraccoon.mementomoricalendar.data.remainingWeeks
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_REMAINING_WEEKS_KEY
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MementoMoriAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MementoMoriAppWidget()

    private val coroutineScope = MainScope()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgetUsingLatestValues(context)
    }

    private fun updateWidgetUsingLatestValues(context: Context) {
        coroutineScope.launch {
            context.userSettingsDataStore.data.collect {
                val remainingWeeks = it.remainingWeeks() ?: return@collect

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
    }
}
