package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.StatisticsViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.CustomCircularProgress

@Composable
fun StatisticsPage(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val percentageLived by viewModel.percentageLived.collectAsStateWithLifecycle()

    StatisticsPageContent(
        modifier = modifier,
        percentageLived = percentageLived
    )
}

@Composable
fun StatisticsPageContent(
    modifier: Modifier = Modifier,
    percentageLived: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your life",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CustomCircularProgress(
            modifier = Modifier.height(250.dp),
            text = "Lived: $percentageLived%",
            progress = percentageLived / 100F
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun StatisticsPageContentPreview() {
    StatisticsPageContent(
        modifier = Modifier.fillMaxSize(),
        percentageLived = 38
    )
}