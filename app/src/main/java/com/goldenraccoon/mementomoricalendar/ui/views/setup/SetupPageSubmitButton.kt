package com.goldenraccoon.mementomoricalendar.ui.views.setup

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SetupPageSubmitButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean,
    text: String,
    icon: ImageVector,
    iconDescription: String
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = onClick,
        enabled = isEnabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = icon,
                contentDescription = iconDescription
            )
        }
    }
}