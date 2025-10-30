package com.anantborad.campuscompass.campus.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anantborad.campuscompass.campus.data.networking.*
import com.anantborad.campuscompass.campus.data.networking.dto.sub.ProgramPercentage
import com.anantborad.campuscompass.campus.presentation.models.*
import com.anantborad.campuscompass.campus.presentation.viewmodels.ResultsViewModel
import com.anantborad.campuscompass.core.ui.theme.CampusCompassTheme

@OptIn(ExperimentalMaterial3Api::class) @Composable fun CollegeDetailScreen(
	viewModel: ResultsViewModel,
	collegeId: String,
	onBackClick: () -> Unit = {},
	onFavoriteClick: () -> Unit,
) {
	val spacing = 20.dp
	val colleges = viewModel.colleges.value
	val isLoading = viewModel.isLoading.value

	// Find the college by ID
	val college = colleges.find { it.id == collegeId }
	val isFavorite = viewModel.favorites.value.contains(college)

	if (isLoading) {
		// Show a loading indicator while colleges are loading
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator()
		}
		return
	}

	if (college == null) {
		// If college not found after loading, show message
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			Text(
				"College not found",
				style = MaterialTheme.typography.titleMedium
			)
		}
		return
	}

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						college.name,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				},
				navigationIcon = {
					IconButton(onClick = onBackClick) {
						Icon(
							Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Back"
						)
					}
				},
				actions = {
					IconButton(onClick = onFavoriteClick) {
						Icon(
							imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
							contentDescription = if (isFavorite) "Remove Favorite" else "Add Favorite",
							tint = if (isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current
						)
					}
				})
		}) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.padding(16.dp)
		) {
			// === Basic Info ===
			SectionHeader("Location & Aliases")
			InfoRow(
				"City",
				college.city
			)
			InfoRow(
				"State",
				college.state.toState()?.fullName ?: college.state
			)
			InfoRow(
				"Zip",
				college.zip
			)
			if (college.alias.isNotBlank()) InfoRow(
				"Also Known As",
				college.alias
			)

			Spacer(Modifier.height(24.dp))

			// === Admissions ===
			SectionHeader("Admissions")
			college.admissionRate?.let {
				InfoRow(
					"Admission Rate",
					"%.1f%%".format(it * 100)
				)
			}
			college.testScores?.let { scores ->
				SectionHeader("Test Scores")
				MiniTable(scores.toMap())
			}

			Spacer(Modifier.height(20.dp))

			// === Academics ===
			SectionHeader("Academics")
			InfoRow(
				"Highest Degree Awarded",
				college.highestAwardedDegree?.description ?: "N/A"
			)
			InfoRow(
				"Classification",
				college.classification?.description ?: "N/A"
			)
			InfoRow(
				"Type (Ownership)",
				college.control?.displayName ?: "N/A"
			)
			college.programPercentage?.toMap()?.let { programs ->
				SectionHeader("Programs (Proportion of Students Enrolled)")
				MiniTable(
					programs.mapValues { (_, value) ->
						// Safely convert Any to Float/Double for formatting
						val number = when (value) {
							is Float -> value * 100
							is Double -> value * 100
							is Int -> value.toFloat() * 100
							else -> 0f
						}
						"%.1f%%".format(number)
					})
			}

			Spacer(Modifier.height(spacing))

			// === Cost & Financial Aid ===
			SectionHeader("Cost & Financial Aid")
			InfoRow(
				"Room & Board",
			        college.roomBoard?.let { "$%,d".format(it) } ?: "N/A")
			InfoRow(
				"Books & Supplies",
			        college.booksSupplies?.let { "$%,d".format(it) } ?: "N/A")
			InfoRow(
				"Other Expenses",
			        college.otherExpenses?.let { "$%,d".format(it) } ?: "N/A")
			InfoRow(
				"Students w/ Pell Grant",
			        college.studentsWithPellGrant?.let { "%.1f%%".format(it * 100) } ?: "N/A")
			if (college.netPriceByIncome.isNotEmpty()) {
				SectionHeader("Net Price by Income")
				MiniTable(college.netPriceByIncome.toNetPriceDisplay())
			}

			Spacer(Modifier.height(spacing))

			// === Outcomes ===
			SectionHeader("Outcomes")
			college.graduationRate?.let {
				InfoRow(
					"Graduation Rate",
					"%.1f%%".format(it * 100)
				)
			}
			college.retentionRate?.let {
				InfoRow(
					"Retention Rate",
					"%.1f%%".format(it * 100)
				)
			}

			Spacer(Modifier.height(spacing))

			// === Demographics ===
			SectionHeader("Demographics")
			InfoRow(
				"Type",
				SchoolSizeRange.fromCode(college.size)?.displayName ?: "N/A"
			)
			InfoRow(
				"Minority-Serving",
				college.minorityServingInterest?.description ?: "N/A"
			)
			InfoRow(
				"Religious Affiliation",
				college.religiousAffiliation?.displayName ?: "N/A"
			)
			InfoRow(
				"Gender Preference",
				college.genderPreference?.displayName ?: "N/A"
			)
			college.demographics?.let { demo ->
				SectionHeader("Demographic Breakdown")
				MiniTable(demo.toDisplay())
			}

			Spacer(Modifier.height(spacing))

			// === Campus Info ===
			SectionHeader("Campus Info & Location")
			InfoRow(
				"Main Campus",
				if (college.mainCampus) "Yes" else "No"
			)
			InfoRow(
				"Online Only",
				if (college.onlineOnly) "Yes" else "No"
			)
			InfoRow(
				"Locale",
				college.locale?.description ?: "N/A"
			)
			InfoRow(
				"Region",
				Region.fromId(college.regionId)?.displayName ?: "N/A"
			)
			if (college.latitude != null && college.longitude != null) {
				InfoRow(
					"Coordinates",
					"%.3f, %.3f".format(
						college.latitude,
						college.longitude
					)
				)
			}

			Spacer(Modifier.height(40.dp))
		}
	}
}

@Composable private fun SectionHeader(title: String) {
	Spacer(Modifier.size(8.dp))
	Text(
		text = title,
		style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
		color = MaterialTheme.colorScheme.primary,
		modifier = Modifier.padding(bottom = 8.dp)
	)
}

@Composable private fun InfoRow(
	label: String,
	value: String,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 6.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.weight(1f)
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.End,
			modifier = Modifier.weight(1f)
		)
	}
}

@Composable fun MiniTable(
	data: Map<String, Any>,
	maxHeight: Dp = 150.dp,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(max = maxHeight)
			.verticalScroll(rememberScrollState())
			.padding(start = 8.dp)
	) {
		Column {
			data.forEach { (key, value) ->
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 2.dp),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = key,
						style = MaterialTheme.typography.bodySmall,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						modifier = Modifier.weight(1f)
					)
					Text(
						text = value.toString(),
						style = MaterialTheme.typography.bodySmall,
						color = MaterialTheme.colorScheme.onSurface,
						textAlign = TextAlign.End,
						modifier = Modifier.weight(1f)
					)
				}
			}
		}
	}
}

@Preview(
	showBackground = true,
	showSystemUi = true
) @Composable fun CollegeDetailScreenPreview() {
	// Mock programs data
	val programPercentage = ProgramPercentage(
		agriculture = 0.1f,
		computer = 0.25f,
		businessMarketing = 0.15f,
		engineering = 0.2f,
		health = 0.1f,
		socialScience = 0.05f,
		education = 0.05f,
		architecture = 0.05f,
		military = 0.05f
	)

	val raceEthnicityDemo = RaceEthnicity(
		white = 0.5f,
		black = 0.2f,
		hispanic = 0.15f,
		asian = 0.1f,
		aian = 0.01f,
		nhpi = 0.01f,
		twoOrMore = 0.02f,
		nonResidentAlien = 0.01f,
		unknown = 0f
	)

	val ageStatsDemo = AgeStats(
		avgEntryAge = 18,
		avgEntryAgeSquared = 324,
		over23AtEntry = 0.05f
	)

	val genderStatsDemo = GenderStats(
		men = 0.48f,
		women = 0.52f,
		femaleShare = 0.52f,
		marriedShare = 0.03f,
		dependentShare = 0.85f,
		veteranShare = 0.02f,
		firstGenShare = 0.15f
	)

	val facultyStatsDemo = FacultyStats(
		raceEthnicity = raceEthnicityDemo,
		men = 0.6f,
		women = 0.4f
	)

	val incomeStatsDemo = IncomeStats(
		avgFamilyIncome = 75000,
		medianFamilyIncome = 70000,
		avgFamilyIncomeIndependents = 45000,
		avgFamilyIncomeLog = 11,
		avgFamilyIncomeIndependentsLog = 10,
		medianHouseholdIncome = 68000,
		medianHouseholdIncomeLog = 11,
		povertyRate = 0.08f,
		unemployment = 0.04f
	)

	val zipStatsDemo = ZipStats(
		shareWhite = 0.6f,
		shareBlack = 0.2f,
		shareAsian = 0.1f,
		shareHispanic = 0.08f,
		shareBachelorsDegree = 0.35f,
		shareProfessionalDegree = 0.1f,
		shareBornUS = 0.9f
	)

	val collegeDemographicsDemo = CollegeDemographics(
		raceEthnicity = raceEthnicityDemo,
		age = ageStatsDemo,
		gender = genderStatsDemo,
		faculty = facultyStatsDemo,
		income = incomeStatsDemo,
		zipStats = zipStatsDemo,
		studentFacultyRatio = 15f
	)

	// Mock college object
	val testCollege = College(
		id = "1",
		name = "Sample University",
		alias = "SU",
		city = "Sample City",
		state = "SC",
		zip = "12345",
		regionId = 5,
		locale = Locale.CITY_MIDSIZE,
		classification = CarnegieBasic.DOCTORAL_HIGH,
		highestAwardedDegree = HighestAwardedDegree.BACHELORS,
		size = SchoolSizeRange.FOUR_YEAR_MEDIUM_RESIDENTIAL.code,
		control = Control.PUBLIC,
		genderPreference = GenderPreference.COED,
		religiousAffiliation = ReligiousAffiliation.NOT_APPLICABLE,
		programPercentage = programPercentage,
		demographics = collegeDemographicsDemo,
		admissionRate = 0.25,
		testScores = StandardizedTestScores(
			satAverage = 1300f,
			act25thPercentile = 27f,
			act75thPercentile = 32f,
			actMidpoint = 29.5f
		),
		roomBoard = 12000,
		booksSupplies = 1200,
		otherExpenses = 800,
		studentsWithPellGrant = 0.3f,
		netPriceByIncome = mapOf(
			"0-30000_public" to 5000,
			"30001-48000_public" to 10000,
			"48001-75000_public" to 15000,
			"75001-110000_public" to 20000,
			"110000-plus_public" to 25000,
			"0-30000_private" to 10000,
			"30001-48000_private" to 15000,
			"48001-75000_private" to 20000,
			"75001-110000_private" to 25000,
			"110000-plus_private" to 30000
		),
		graduationRate = 0.85,
		retentionRate = 0.9,
		mainCampus = true,
		onlineOnly = false,
		latitude = 34.123,
		longitude = -92.456
	)

	CampusCompassTheme {
		fakeViewModel.addCollege(testCollege)
		CollegeDetailScreen(
			collegeId = "1",
			viewModel = fakeViewModel,
			onFavoriteClick = {},
			onBackClick = {})
	}
}


