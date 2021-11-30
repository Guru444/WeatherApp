package com.kafein.weatherapp


import com.kafein.weatherapp.WeatherAPI
import com.kafein.weatherapp.model.response.CityListResponse
import com.kafein.weatherapp.model.response.SearchCityResultResponse
import com.kafein.weatherapp.model.response.WeatherResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {


    private val BASE_URL = "https://dataservice.accuweather.com/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getSearchCityResult(searchTerm: String): Single<SearchCityResultResponse> {
        return api.locationCitySearch(searchTerm = searchTerm)
    }

    fun cityList(): Single<CityListResponse> {
        return api.cityList()
    }

    fun getWeatherInfo(key: String) : Single<WeatherResponse> {
        return api.currentConditionsWeather(key)
    }
}