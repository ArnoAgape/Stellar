package com.openclassrooms.stellarforecast.di

import com.openclassrooms.stellarforecast.data.network.WeatherClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Provides a singleton instance of Retrofit for network communication
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
        .client(provideOkHttpClient()) // Uses a separate function for OkHttpClient configuration
        .build()
    }

    // Provides a singleton instance of WeatherClient using Retrofit
    @Singleton
    @Provides
    fun provideWeatherClient(retrofit: Retrofit): WeatherClient {
        return retrofit.create(WeatherClient::class.java)
    }

    // Private function to configure OkHttpClient with an interceptor for logging
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }.build()
    }
}