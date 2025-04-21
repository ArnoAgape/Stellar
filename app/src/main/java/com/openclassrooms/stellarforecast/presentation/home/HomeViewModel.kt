package com.openclassrooms.stellarforecast.presentation.home

import androidx.lifecycle.ViewModel
import com.openclassrooms.stellarforecast.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.openclassrooms.stellarforecast.domain.model.WeatherReportModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: WeatherRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(HomeUISTate())
    val uiState: StateFlow<HomeUISTate> = _uiState.asStateFlow()

    init {
        getForecastData()
    }

    private fun getForecastData() {
        val latitude = 48.844304
        val longitude = 2.374377
        dataRepository.fetchForecastData(latitude, longitude)
            .onEach { forecastUpdate ->
                _uiState.update { currentState ->
                    currentState.copy(
                        forecast = forecastUpdate
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}

data class HomeUISTate(
    val forecast: List<WeatherReportModel> = emptyList()
)