package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SingleChoiceSegmentedButton(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    onOptionSelected(index)
                },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SingleChoiceSegmentedButtonPreview() {
    SingleChoiceSegmentedButton(
        modifier = Modifier
            .padding(top = 50.dp)
            .padding(horizontal = 8.dp),
        options = listOf("Lived", "Remaining"),
        selectedIndex = 0,
        onOptionSelected = { }
    )
}