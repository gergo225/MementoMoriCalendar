package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_LIVED_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_OF_DAY_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_OF_MONTH_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_OF_WEEK_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_REMAINING_WEEKS_KEY
import com.goldenraccoon.mementomoricalendar.widget.current.CurrentPeriodWidget
import com.goldenraccoon.mementomoricalendar.widget.total.TotalLifeWidget
import com.goldenraccoon.mementomoricalendar.widget.weeks.RemainingWeeksWidget

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

        suspend fun updateTotalLifeWidget(percentageLived: Int, context: Context) {
            GlanceAppWidgetManager(context).getGlanceIds(TotalLifeWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { pref ->
                        pref[stringPreferencesKey(WIDGET_PERCENTAGE_LIVED_KEY)] =
                            percentageLived.toString()
                    }
                    TotalLifeWidget().updateAll(context)
                }
        }

        suspend fun updateCurrentPeriodWidget(dayPercentage: Int, weekPercentage: Int, monthPercentage: Int, context: Context) {
            GlanceAppWidgetManager(context).getGlanceIds(CurrentPeriodWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { pref ->
                        pref[stringPreferencesKey(WIDGET_PERCENTAGE_OF_DAY_KEY)] = dayPercentage.toString()
                        pref[stringPreferencesKey(WIDGET_PERCENTAGE_OF_WEEK_KEY)] = weekPercentage.toString()
                        pref[stringPreferencesKey(WIDGET_PERCENTAGE_OF_MONTH_KEY)] = monthPercentage.toString()
                    }
                    CurrentPeriodWidget().updateAll(context)
                }
        }
    }
}