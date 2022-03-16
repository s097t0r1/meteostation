package com.zmitrovich.meteostation.di

import com.zmitrovich.meteostation.data.WeatherRepository
import com.zmitrovich.meteostation.data.WeatherRepositoryImpl
import com.zmitrovich.meteostation.data.remote.MockWeatherDataSource
import com.zmitrovich.meteostation.data.remote.WeatherDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindWeatherDataSource(weatherDataSource: MockWeatherDataSource): WeatherDataSource

    @Binds
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}