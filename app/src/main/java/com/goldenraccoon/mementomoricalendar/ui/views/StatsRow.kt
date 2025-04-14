package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme

data class StatItem(val name: String, val value: String)

class StatsRowModel(
    private val items: List<StatItem>
) : Map<String, String> by items.associate({ it.name to it.value })

@Composable
fun StatsRow(
    modifier: Modifier = Modifier,
    stats: StatsRowModel
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            stats.forEach { (header, value) ->
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = value,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = header,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun StatsRowPreview() {
    MementoMoriCalendarTheme {
        StatsRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 50.dp),
            stats = StatsRowModel(
                listOf(
                    StatItem("Years", "56.3"),
                    StatItem("Months", "676"),
                    StatItem("Weeks", "2704"),
                    StatItem("Days", "18.920")
                )
            )
        )
    }
}