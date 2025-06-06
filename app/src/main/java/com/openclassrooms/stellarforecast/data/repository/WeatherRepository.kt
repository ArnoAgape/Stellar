package com.openclassrooms.stellarforecast.data.repository

import android.util.Log
import com.openclassrooms.stellarforecast.data.network.WeatherClient
import com.openclassrooms.stellarforecast.domain.model.WeatherReportModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRepository(private val dataService: WeatherClient) {
    private val apikey = "f59de60066c77cd44a5716d000349590"

    fun fetchForecastData(lat: Double, lng: Double): Flow<List<WeatherReportModel>> = flow {
        val result = dataService.getWeatherByPosition(lat, lng, apikey)
        val model = result.body()?.toDomainModel() ?: throw Exception("Invalid data")

        emit(model)
    }.catch { error ->
        Log.e("WeatherRepository", error.message ?: "")
    }
}