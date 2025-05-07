package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_REMAINING_WEEKS_KEY

class WidgetUtils {
    companion object {
        suspend fun updateRemainingWeeksWidgets(remainingWeeks: Int, context: Context) {
            GlanceAppWidgetManager(context).getGlanceIds(RemainingWeeksWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { pref ->
                        pref[stringPreferencesKey(WIDGET_REMAINING_WEEKS_KEY)] =
                            remainingWeeks.toString()
                    }
                    RemainingWeeksWidget().updateAll(context)
                }
        }
    }
}