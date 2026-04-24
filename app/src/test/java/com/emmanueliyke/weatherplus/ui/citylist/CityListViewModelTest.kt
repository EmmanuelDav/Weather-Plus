package com.emmanueliyke.weatherplus.ui.citylist

import app.cash.turbine.test
import com.emmanueliyke.weatherplus.ui.citylist.CityListIntent
import com.emmanueliyke.weatherplus.ui.citylist.CityListViewModel
import com.emmanueliyke.weatherplus.util.FakeWeatherRepository
import com.emmanueliyke.weatherplus.util.TestDispatcherRule
import com.emmanueliyke.weatherplus.util.fakeCityWeather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var repository: FakeWeatherRepository
    private lateinit var viewModel: CityListViewModel

    private val lagos = fakeCityWeather(cityId = 1, name = "Lagos")
    private val london = fakeCityWeather(cityId = 2, name = "London")
    private val dubai = fakeCityWeather(cityId = 3, name = "Dubai", isFavorite = true)

    @Before
    fun setup() {
        repository = FakeWeatherRepository()
        repository.setCities(listOf(lagos, london, dubai))
        viewModel = CityListViewModel(repository)
    }

    @Test
    fun `initial state has loading true`() = runTest {
        val freshRepo = FakeWeatherRepository()
        val freshViewModel = CityListViewModel(freshRepo)
        assertTrue(freshViewModel.uiState.value.isLoading)
    }

    @Test
    fun `cities are emitted from repository`() = runTest {
        viewModel.uiState.test {
            dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
            val state = awaitItem()
            assertEquals(3, state.cities.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query filters cities by name`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(CityListIntent.OnSearchQueryChanged("lag"))

        val filtered = viewModel.uiState.value.filteredCities
        assertEquals(1, filtered.size)
        assertEquals("Lagos", filtered.first().name)
    }

    @Test
    fun `search query is case insensitive`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(CityListIntent.OnSearchQueryChanged("LONDON"))

        val filtered = viewModel.uiState.value.filteredCities
        assertEquals(1, filtered.size)
        assertEquals("London", filtered.first().name)
    }

    @Test
    fun `clear search resets query`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(CityListIntent.OnSearchQueryChanged("Lagos"))
        viewModel.onIntent(CityListIntent.OnClearSearch)

        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `empty search returns all cities`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(CityListIntent.OnSearchQueryChanged(""))

        assertEquals(3, viewModel.uiState.value.filteredCities.size)
    }

    @Test
    fun `toggle favorite calls repository`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(CityListIntent.OnToggleFavorite(lagos.cityId, false))
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(repository.toggleFavoriteCalled)
        assertEquals(lagos.cityId, repository.lastToggledCityId)
    }

    @Test
    fun `offline state is set when refresh fails`() = runTest {
        repository.shouldFailRefresh = true
        val freshViewModel = CityListViewModel(repository)
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(freshViewModel.uiState.value.isOffline)
    }

    @Test
    fun `offline state is false when refresh succeeds`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isOffline)
    }

    @Test
    fun `search with no match returns empty filtered list`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(CityListIntent.OnSearchQueryChanged("xyz"))

        assertTrue(viewModel.uiState.value.filteredCities.isEmpty())
        assertTrue(viewModel.uiState.value.isEmpty)
    }
}