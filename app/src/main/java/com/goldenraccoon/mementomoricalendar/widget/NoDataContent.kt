package com.goldenraccoon.mementomoricalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text

@Composable
fun NoDataContent() {
    Column(
        modifier = GlanceModifier.padding(vertical = 4.dp)
            .padding(horizontal = 8.dp)
            .background(GlanceTheme.colors.background)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "No data",
            modifier = GlanceModifier.padding(bottom = 8.dp)
        )

        Button(
            "Finish setup",
            onClick = WidgetUtils.startAppAction
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 180, heightDp = 90)
@Composable
fun NoDataContentPreview() {
    NoDataContent()
}
