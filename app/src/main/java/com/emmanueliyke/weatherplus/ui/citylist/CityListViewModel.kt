package com.emmanueliyke.weatherplus.ui.citylist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmanueliyke.weatherplus.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CityListUiState(isLoading = true))
    val uiState: StateFlow<CityListUiState> = _uiState.asStateFlow()

    init {
        observeCities()
        refresh()
    }

    fun onIntent(intent: CityListIntent) {
        when (intent) {
            is CityListIntent.OnSearchQueryChanged ->
                _uiState.update { it.copy(searchQuery = intent.query) }

            is CityListIntent.OnToggleFavorite ->
                toggleFavorite(intent.cityId, intent.isFavorite)

            CityListIntent.OnRefresh -> refresh()

            CityListIntent.OnClearSearch ->
                _uiState.update { it.copy(searchQuery = "") }
        }
    }

    private fun observeCities() {
        repository.observeCities()
            .onEach { cities ->
                _uiState.update {
                    it.copy(
                        cities = cities,
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.refreshCities()
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isOffline = false) }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isOffline = true,
                            error = "No internet — showing cached data"
                        )
                    }
                }
        }
    }

    private fun toggleFavorite(cityId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(cityId, isFavorite)
        }
    }
}
