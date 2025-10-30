package com.anantborad.campuscompass.campus.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anantborad.campuscompass.campus.presentation.ui.components.*
import com.anantborad.campuscompass.campus.presentation.viewmodels.FormViewModel
import com.anantborad.campuscompass.core.ui.theme.CampusCompassTheme
import com.anantborad.campuscompass.core.ui.theme.roundButton

@Composable fun FinancialScreen(
	viewModel: FormViewModel,
	onNext: () -> Unit,
	onIconSelected: (Int) -> Unit = {},
	enabledIcons: Map<Int, Boolean> = emptyMap(),
) {
	AppScreen(
		headerText = "Financial Profile",
		subHeaderText = "Your Financial Preferences and Limits",
		progress = 0.75f,
		progressDescription = "Step 3 of 4",
		onIconSelected = onIconSelected,
		enabledIcons = enabledIcons
	) {
		Spacer(modifier = Modifier.size(5.dp))
		Column(
			modifier = Modifier.width(200.dp),
			verticalArrangement = Arrangement.spacedBy(6.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			val options = listOf(
				"0 - 30,000",
				"30,001 - 48,000",
				"48,001 - 75,000",
				"75,001 - 110,000",
				"110,000 +"
			).map { "$$it" }
			AppTextDropdown(
				label = "Income",
				value = viewModel.incomeBracket,
				onValueChange = viewModel::updateIncome,
				options = options,
				placeholder = "Income",
				supportingText = "Total Household Income"
			)

			Spacer(modifier = Modifier.size(24.dp))
			AppTextField(
				label = "Max Tuition",
				placeholder = "Input",
				supportingText = "Optional but helps filter out certain colleges",
				value = viewModel.maxTuition,
				onValueChange = viewModel::updateTuition
			)
		}

		var loanOption by remember { mutableStateOf(viewModel.loanOption) }
		ChoiceCard(
			header = "Are you willing to take loans?",
			subheader = "This will further help determine your financial profile",
			options = listOf(
				"Yes",
				"No",
				"Maybe"
			),
			onOptionSelected = {
				loanOption = it
				viewModel.updateLoanOption(it)
			},
			modifier = Modifier.padding(
				horizontal = 8.dp,
				vertical = 4.dp
			)
		)

		AppButton(
			text = "Next",
			icon = Icons.AutoMirrored.Filled.ArrowForward,
			shape = MaterialTheme.shapes.roundButton,
			modifier = Modifier.padding(vertical = 8.dp),
			onClick = onNext
		)

//		DropDownCard(
//			header = "Want a more accurate estimate?",
//			subheader = "Use the Official Net Price Calculator",
//			iconPainter = painterResource(R.drawable.dollar_sign)
//		) {
//			AppButton(
//				text = "Open Calculator",
//				icon = Icons.Filled.Link,
//				shape = MaterialTheme.shapes.squareButton,
//				modifier = Modifier.align(Alignment.CenterHorizontally),
//				onClick = onCalcClick
//			)
//		}
	}
}

@SuppressLint("ViewModelConstructorInComposable") @Preview(showBackground = true) @Composable
private fun FinancialScreenPreview() {
	val viewModel = FormViewModel()
	CampusCompassTheme {
		FinancialScreen(
			viewModel = viewModel,
			onNext = {},       // dummy nav lambda
		)
	}
}
