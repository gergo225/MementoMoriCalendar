package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.proto.UserSettings
import com.goldenraccoon.mementomoricalendar.util.Constants
import java.io.File
import java.util.concurrent.TimeUnit

class MementoMoriAppWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>
        get() = MementoMoriGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
                WidgetContent()
            }
        }
    }

    @Composable
    private fun WidgetContent() {
        val userSettings = currentState<UserSettings>()
        // TODO: update widget when value changes
        val remainingWeeks = remember { userSettings.remainingWeeks() }

        Column(
            modifier = GlanceModifier
                .padding(4.dp)
                .background(GlanceTheme.colors.widgetBackground)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$remainingWeeks",
               style = TextStyle(fontSize = 24.sp)
                )
            Text("Weeks Remaining")
        }
    }
}

object MementoMoriGlanceStateDefinition : GlanceStateDefinition<UserSettings> {
    private const val FILE_NAME = "user_settings"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<UserSettings> {
        return context.userSettingsDataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.applicationContext.filesDir, "datastore/$FILE_NAME")
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