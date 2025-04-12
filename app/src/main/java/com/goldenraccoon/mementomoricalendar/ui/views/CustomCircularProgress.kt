package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme

@Composable
fun CustomCircularProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    text: String,
    strokeWidth: Dp = 24.dp
) {
    Box(
        modifier = modifier.aspectRatio(1F),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { progress },
            strokeWidth = strokeWidth,
            gapSize = (-24).dp,
            trackColor = MaterialTheme.colorScheme.onTertiary,
            color = MaterialTheme.colorScheme.tertiary
        )

        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CustomCircularProgressPreview() {
    MementoMoriCalendarTheme {
        CustomCircularProgress(
            modifier = Modifier.size(300.dp),
            progress = 0.44F,
            text = "Lived: 44%"
        )
    }
}