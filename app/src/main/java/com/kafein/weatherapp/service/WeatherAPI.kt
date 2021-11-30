package com.kafein.weatherapp

import com.kafein.weatherapp.model.response.CityListResponse
import com.kafein.weatherapp.model.response.SearchCityResultResponse
import com.kafein.weatherapp.model.response.WeatherResponse
import com.kafein.weatherapp.util.WeatherConstants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherAPI {

    companion object{
        const val CITY_LIST = "locations/v1/adminareas/{TR}"
        const val LOCATION_CITY_SEARCH = "locations/v1/cities/search"
        const val CURRENT_CONDITIONS = "currentconditions/v1/{key}"
    }

    @GET(LOCATION_CITY_SEARCH)
    fun locationCitySearch(@Query("apikey") apikey: String = WeatherConstants.API_KEY,
                     @Query("q") searchTerm: String,
                     @Query("language") language: String = WeatherConstants.LANGUAGE,
                     @Query("details") details: Boolean = false): Single<SearchCityResultResponse>

    @GET(CITY_LIST)
    fun cityList(@Path("TR") countryTR: String = WeatherConstants.SPESIFIC_COUNTRY,
                 @Query("apikey") apikey: String = WeatherConstants.API_KEY,
                 @Query("language") language: String = WeatherConstants.LANGUAGE) : Single<CityListResponse>

    @GET(CURRENT_CONDITIONS)
    fun currentConditionsWeather(@Path("key") key: String,
                                 @Query("apikey") apikey: String = WeatherConstants.API_KEY,
                                 @Query("language") language: String = WeatherConstants.LANGUAGE,
                                 @Query("details") details: Boolean = false) : Single<WeatherResponse>
}