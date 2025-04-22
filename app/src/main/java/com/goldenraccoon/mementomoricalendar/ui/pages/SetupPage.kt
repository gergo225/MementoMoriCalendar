package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.SetupViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.setup.BirthdaySetupContent
import com.goldenraccoon.mementomoricalendar.ui.views.setup.LifeExpectancySetupContent

@Composable
fun SetupPage(
    modifier: Modifier = Modifier,
    viewModel: SetupViewModel = hiltViewModel(),
    onFinishClicked: () -> Unit
) {
    val viewState = viewModel.viewState

    Box(
        modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (viewState) {
            SetupViewModel.ViewState.Birthday -> {
                BirthdaySetupContent(
                    onDateSelected = { dateMillis ->
                        viewModel.onDateSelected(dateMillis)
                    },
                    isButtonEnabled = viewModel.isContinueEnabled,
                    onContinueClicked = {
                        viewModel.selectedDateMillis?.let {
                            viewModel.setBirthday(it)
                        }
                    }
                )
            }

            SetupViewModel.ViewState.LifeExpectancy -> {
                LifeExpectancySetupContent(
                    lifeExpectancyText = viewModel.lifeExpectancyInput,
                    onLifeExpectancyChange = {
                        viewModel.onLifeExpectancyInputChange(it)
                    },
                    isButtonEnabled = viewModel.isFinishEnabled,
                    onFinishClicked = {
                        viewModel.setLifeExpectancy(viewModel.lifeExpectancyInput)
                        onFinishClicked()
                    }
                )
            }
        }
    }
}
