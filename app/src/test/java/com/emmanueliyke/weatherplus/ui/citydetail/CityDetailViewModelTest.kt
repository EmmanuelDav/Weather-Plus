package com.emmanueliyke.weatherplus.ui.citydetail


import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.emmanueliyke.weatherplus.util.FakeWeatherRepository
import com.emmanueliyke.weatherplus.util.TestDispatcherRule
import com.emmanueliyke.weatherplus.util.fakeCityWeather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityDetailViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var repository: FakeWeatherRepository
    private lateinit var viewModel: CityDetailViewModel

    private val testCity = fakeCityWeather(cityId = 1, name = "Lagos", isFavorite = false)

    @Before
    fun setup() {
        repository = FakeWeatherRepository()
        repository.setCities(listOf(testCity))

        val savedStateHandle = SavedStateHandle(mapOf("cityId" to testCity.cityId))
        viewModel = CityDetailViewModel(repository, savedStateHandle)
    }

    @Test
    fun `city is loaded from repository on init`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.city)
        assertEquals("Lagos", state.city?.name)
        assertFalse(state.isLoading)
    }

    @Test
    fun `error state when city not found`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("cityId" to 999))
        val freshViewModel = CityDetailViewModel(repository, savedStateHandle)
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = freshViewModel.uiState.value
        assertNull(state.city)
        assertNotNull(state.error)
    }

    @Test
    fun `toggle favorite flips isFavorite state`() = runTest {
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.city?.isFavorite == true)

        viewModel.toggleFavorite()
        dispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.city?.isFavorite == true)
    }

    @Test
    fun `loading is true on init before data loads`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("cityId" to testCity.cityId))
        val freshViewModel = CityDetailViewModel(repository, savedStateHandle)

        assertTrue(freshViewModel.uiState.value.isLoading)
    }
}