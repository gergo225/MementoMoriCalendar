package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_BIRTHDAY_MILLIS_KEY
import java.io.File

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
        val preferences = currentState<Preferences>()
        val birthdayMillis = remember {
            preferences[stringPreferencesKey(WIDGET_BIRTHDAY_MILLIS_KEY)] ?: "error"
        }

        Column(
            modifier = GlanceModifier
                .padding(4.dp)
                .background(GlanceTheme.colors.widgetBackground)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = birthdayMillis,
                style = TextStyle(fontSize = 24.sp)
            )
            Text("Weeks Remaining")
        }
    }
}

object MementoMoriGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    private const val FILE_NAME = "widget_preference"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        return context.dataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.applicationContext.filesDir, "datastore/$FILE_NAME")
    }

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = FILE_NAME)
}
