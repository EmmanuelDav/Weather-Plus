package com.emmanueliyke.weatherplus.ui.citydetail




import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmanueliyke.weatherplus.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    private val repository: WeatherRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cityId: Int = checkNotNull(savedStateHandle["cityId"])

    private val _uiState = MutableStateFlow(CityDetailUiState())
    val uiState: StateFlow<CityDetailUiState> = _uiState.asStateFlow()

    init {
        loadCity()
    }

    fun toggleFavorite() {
        val city = _uiState.value.city ?: return
        viewModelScope.launch {
            repository.toggleFavorite(city.cityId, !city.isFavorite)
            loadCity()
        }
    }

    private fun loadCity() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val city = repository.getCityById(cityId)
            if (city != null) {
                _uiState.update { it.copy(city = city, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "City not found") }
            }
        }
    }
}