package com.emmanueliyke.weatherplus.ui.citylist


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emmanueliyke.weatherplus.ui.components.CityWeatherCard
import com.emmanueliyke.weatherplus.ui.components.EmptyState
import com.emmanueliyke.weatherplus.ui.components.OfflineBanner
import com.emmanueliyke.weatherplus.ui.components.WeatherSearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    onCityClick: (Int) -> Unit,
    viewModel: CityListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()


    Scaffold(
        containerColor = Color(0xFF1C1C1E),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "WeatherNow",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1C1E)
                )
            )
        }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.onIntent(CityListIntent.OnRefresh) },
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                OfflineBanner(isOffline = uiState.isOffline)

                WeatherSearchBar(
                    query = uiState.searchQuery,
                    onQueryChanged = {
                        viewModel.onIntent(CityListIntent.OnSearchQueryChanged(it))
                    },
                    onClearQuery = {
                        viewModel.onIntent(CityListIntent.OnClearSearch)
                    }
                )

                when {
                    uiState.isLoading && uiState.cities.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF4FC3F7))
                        }
                    }

                    uiState.isEmpty -> {
                        EmptyState(query = uiState.searchQuery)
                    }

                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(
                                items = uiState.filteredCities,
                                key = { it.cityId }
                            ) { city ->
                                CityWeatherCard(
                                    city = city,
                                    onCityClick = onCityClick,
                                    onToggleFavorite = { id, current ->
                                        viewModel.onIntent(
                                            CityListIntent.OnToggleFavorite(id, !current)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}