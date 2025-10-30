package com.anantborad.campuscompass.campus.presentation.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.anantborad.campuscompass.campus.presentation.models.College
import com.anantborad.campuscompass.campus.presentation.models.Filter
import com.anantborad.campuscompass.campus.presentation.ui.components.AppScreen
import com.anantborad.campuscompass.campus.presentation.ui.components.CollegeListScreen
import com.anantborad.campuscompass.campus.presentation.viewmodels.ResultsViewModel
import com.anantborad.campuscompass.core.ui.theme.CampusCompassTheme

@RequiresApi(Build.VERSION_CODES.TIRAMISU) @Composable fun FavoritesScreen(
	viewModel: ResultsViewModel,
	onCollegeClick: (College) -> Unit = {},
	onIconSelected: (Int) -> Unit = {},
) {
	val favorites = viewModel.favorites.value

	AppScreen(
		screenTitle = "Favorites",
		headerText = "Your Saved Colleges",
		subHeaderText = "Tap ♥ again to remove",
		progress = 1.0f,
		progressDescription = "${favorites.size} favorites",
		scrollableContent = false,
		onIconSelected = onIconSelected
	) { header ->
		CollegeListScreen(
			colleges = favorites.toList(),
			favorites = favorites,
			isLoading = false,
			showFilter = false, // hide the filter menu
			filter = null,
			requiredFields = emptySet(),
			onFavoriteClick = { viewModel.toggleFavorite(it) }, // remove from favorites automatically
			onCollegeClick = onCollegeClick,
			headerContent = header,
		)
	}
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU) @Preview(showBackground = true) @Composable
private fun FavoritesScreenPreview() {
	// Sample colleges
	val sampleColleges = listOf(
		College(
			id = "1",
			name = "Stanford University",
			city = "Stanford",
			state = "CA",
			admissionRate = 0.04
		),
		College(
			id = "2",
			name = "MIT",
			city = "Cambridge",
			state = "MA",
			admissionRate = 0.07
		),
		College(
			id = "3",
			name = "UC Berkeley",
			city = "Berkeley",
			state = "CA",
			admissionRate = 0.11
		)
	)

	// Pretend these are favorites
	val favoritesSet = setOf(
		sampleColleges[0],
		sampleColleges[2]
	)

	CampusCompassTheme {
		AppScreen(
			screenTitle = "Favorites",
			headerText = "Your Saved Colleges",
			subHeaderText = "Tap ♥ again to remove",
			progress = 1f,
			progressDescription = "${favoritesSet.size} favorites",
			scrollableContent = false
		) { header ->
			CollegeListScreen(
				colleges = favoritesSet.toList(),
				favorites = favoritesSet,
				isLoading = false,
				showFilter = false,  // hide filter
				filter = null,
				requiredFields = emptySet(),
				onFavoriteClick = {},
				onCollegeClick = {},
				headerContent = header
			)
		}
	}
}
