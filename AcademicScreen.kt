package com.anantborad.campuscompass.campus.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anantborad.campuscompass.campus.presentation.ui.components.AppButton
import com.anantborad.campuscompass.campus.presentation.ui.components.AppScreen
import com.anantborad.campuscompass.campus.presentation.ui.components.AppTextDropdown
import com.anantborad.campuscompass.campus.presentation.ui.components.AppTextField
import com.anantborad.campuscompass.campus.presentation.viewmodels.FormViewModel
import com.anantborad.campuscompass.core.ui.theme.CampusCompassTheme
import com.anantborad.campuscompass.core.ui.theme.roundButton

@Composable fun AcademicScreen(
	viewModel: FormViewModel,
	onNext: () -> Unit,
	onIconSelected: (Int) -> Unit = {},
	enabledIcons: Map<Int, Boolean> = emptyMap()
) {
	AppScreen(
		headerText = "Academic Profile",
		subHeaderText = "Quickly Gauge Academic Fit",
		progress = 0.5f,
		progressDescription = "Step 2 of 4",
		onIconSelected = onIconSelected,
		enabledIcons = enabledIcons
	) {
		Column(
			modifier = Modifier.width(200.dp),
			verticalArrangement = Arrangement.spacedBy(6.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AppTextField(
				label = "SAT",
				placeholder = "Input",
				supportingText = "Out of 1600",
				value = viewModel.satScore,
				onValueChange = viewModel::updateSAT
			)
			AppTextField(
				label = "ACT",
				placeholder = "Input",
				supportingText = "Composite out of 36",
				value = viewModel.actScore,
				onValueChange = viewModel::updateACT
			)
			AppTextField(
				label = "GPA",
				placeholder = "Input",
				supportingText = "Unweighted on a 4.0 Scale",
				value = viewModel.gpa,
				onValueChange = viewModel::updateGPA
			)

			val majors = listOf(
				"Math",
				"Science",
				"English",
				"History"
			)
			AppTextDropdown(
				label = "Intended Major",
				value = viewModel.intendedMajor,
				onValueChange = viewModel::updateMajor,
				options = majors,
				placeholder = "Subject"
			)

			Spacer(modifier = Modifier.size(12.dp))
			AppButton(
				text = "Next",
				icon = Icons.AutoMirrored.Filled.ArrowForward,
				shape = MaterialTheme.shapes.roundButton,
				onClick = onNext
			)
		}
	}
}

@SuppressLint("ViewModelConstructorInComposable") @Preview(showBackground = true) @Composable
private fun AcademicScreenPreview() {
	val viewModel = FormViewModel()
	CampusCompassTheme {
		AcademicScreen(
			viewModel = viewModel,
		               onNext = {} // dummy nav lambda
		)
	}
}
