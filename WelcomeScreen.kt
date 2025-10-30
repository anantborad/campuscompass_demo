package com.anantborad.campuscompass.campus.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anantborad.campuscompass.campus.presentation.ui.components.AppButton
import com.anantborad.campuscompass.campus.presentation.ui.components.AppScreen
import com.anantborad.campuscompass.campus.presentation.viewmodels.FormViewModel
import com.anantborad.campuscompass.core.ui.theme.CampusCompassTheme
import com.anantborad.campuscompass.core.ui.theme.roundButton

@Composable fun WelcomeScreen(
	viewModel: FormViewModel,
	onNext: () -> Unit,
	onIconSelected: (Int) -> Unit = {},
	enabledIcons: Map<Int, Boolean> = emptyMap(),
) {
	AppScreen(
		headerText = "Welcome",
		subHeaderText = "We'll help you find the right college for you!",
		progress = 0.25f,
		progressDescription = "Step 1 of 4",
		onIconSelected = onIconSelected,
		enabledIcons = enabledIcons
	) {
		Column {
			Spacer(modifier = Modifier.size(40.dp))
			AppButton(
				text = "Get Started",
				icon = Icons.AutoMirrored.Filled.ArrowForward,
				shape = MaterialTheme.shapes.roundButton,
				onClick = {
					onNext()
				})
			Spacer(modifier = Modifier.size(66.dp))
			AppButton(
				text = "Learn More",
				icon = Icons.Filled.Groups,
				onClick = viewModel::setLearnMoreClicked
			)
		}
	}
}

@SuppressLint("ViewModelConstructorInComposable") @Preview(showBackground = true) @Composable
private fun WelcomeScreenPreview() {
	val viewModel = FormViewModel()
	CampusCompassTheme {
		WelcomeScreen(
			viewModel = viewModel,
		              onNext = {} // dummy nav lambda
		)
	}
}
