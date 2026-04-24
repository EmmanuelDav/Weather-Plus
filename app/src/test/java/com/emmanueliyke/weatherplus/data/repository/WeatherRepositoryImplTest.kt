package com.emmanueliyke.weatherplus.data.repository


import com.emmanueliyke.weatherplus.data.local.dao.CityWeatherDao
import com.emmanueliyke.weatherplus.data.local.entries.CityWeatherEntity
import com.emmanueliyke.weatherplus.data.remote.api.WeatherApiService
import com.emmanueliyke.weatherplus.data.remote.dto.MainDto
import com.emmanueliyke.weatherplus.data.remote.dto.SysDto
import com.emmanueliyke.weatherplus.data.remote.dto.WeatherDto
import com.emmanueliyke.weatherplus.data.remote.dto.WeatherResponseDto
import com.emmanueliyke.weatherplus.data.remote.dto.WindDto
import com.emmanueliyke.weatherplus.data.repository.WeatherRepositoryImpl
import com.emmanueliyke.weatherplus.util.TestDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var repository: WeatherRepositoryImpl
    private val api = mockk<WeatherApiService>()
    private val dao = mockk<CityWeatherDao>(relaxed = true)

    private fun fakeDto(id: Int, name: String) = WeatherResponseDto(
        id = id,
        name = name,
        sys = SysDto(country = "NG"),
        main = MainDto(
            temp = 30.0,
            feelsLike = 28.0,
            humidity = 80,
            pressure = 1012
        ),
        weather = listOf(WeatherDto(description = "Sunny", icon = "01d")),
        wind = WindDto(speed = 5.0),
        visibility = 10000
    )

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(api, dao)
    }

    @Test
    fun `refreshCities succeeds and upserts entities`() = runTest {
        coEvery { dao.getFavoriteCityIds() } returns emptyList()
        coEvery {
            api.getCityWeather(any(), any(), any())
        } returns fakeDto(1, "Lagos")

        val result = repository.refreshCities()

        assertTrue(result.isSuccess)
        coVerify { dao.upsertAll(any()) }
    }

    @Test
    fun `refreshCities preserves favorite state`() = runTest {
        coEvery { dao.getFavoriteCityIds() } returns listOf(1)
        coEvery {
            api.getCityWeather(any(), any(), any())
        } returns fakeDto(1, "Lagos")

        repository.refreshCities()

        coVerify {
            dao.upsertAll(match { entities ->
                entities.any { it.cityId == 1 && it.isFavorite }
            })
        }
    }

    @Test
    fun `refreshCities returns failure on API error`() = runTest {
        coEvery { dao.getFavoriteCityIds() } returns emptyList()

        coEvery {
            api.getCityWeather(any(), any(), any())
        } throws Exception("Network error")


        val result = repository.refreshCities()

        assertTrue(result.isFailure)
    }

    @Test
    fun `toggleFavorite calls dao setFavorite`() = runTest {
        repository.toggleFavorite(cityId = 1, isFavorite = true)


        coVerify { dao.setFavorite(cityId = 1, isFavorite = true) }
    }

    @Test
    fun `getCityById returns null when city not found`() = runTest {
        coEvery { dao.getCityById(999) } returns null

        val result = repository.getCityById(999)

        assertTrue(result == null)
    }

    @Test
    fun `observeCities maps entities to domain models`() = runTest {
        val entity = CityWeatherEntity(
            cityId = 1,
            name = "Lagos",
            country = "NG",
            tempCelsius = 30.0,
            feelsLike = 28.0,
            humidity = 80,
            description = "Sunny",
            iconCode = "01d",
            windSpeed = 5.0,
            visibility = 10000,
            pressure = 1012,
            isFavorite = false,
            lastUpdated = System.currentTimeMillis()
        )

        coEvery { dao.observeAll() } returns flowOf(listOf(entity))

        repository.observeCities().collect { cities ->
            assertTrue(cities.isNotEmpty())
            assertTrue(cities.first().name == "Lagos")
        }
    }
}