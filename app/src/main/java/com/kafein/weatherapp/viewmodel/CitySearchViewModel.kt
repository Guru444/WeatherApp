package com.kafein.weatherapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kafein.weatherapp.model.response.CityListResponse
import com.kafein.weatherapp.model.response.SearchCityResultResponse
import com.kafein.weatherapp.model.response.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CitySearchViewModel(application: Application) : BaseViewModel(application) {

    private val weatherAPIService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    private val _citySearchLiveData = MutableLiveData<SearchCityResultResponse>()
    val citySearchLiveData : LiveData<SearchCityResultResponse> = _citySearchLiveData

    private val _cityListLiveData = MutableLiveData<CityListResponse>()
    val cityListLiveData : LiveData<CityListResponse> = _cityListLiveData

    private val _weatherInfoLiveData = MutableLiveData<WeatherResponse>()
    val weatherInfoLiveData : LiveData<WeatherResponse> = _weatherInfoLiveData

    fun searchCity(searcTerm: String) {
        disposable.add(
            weatherAPIService.getSearchCityResult(searcTerm)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SearchCityResultResponse>() {
                    override fun onSuccess(response: SearchCityResultResponse) {
                        _citySearchLiveData.value = response
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    fun cityList(){
        disposable.add(
            weatherAPIService.cityList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CityListResponse>() {
                    override fun onSuccess(response: CityListResponse) {
                        _cityListLiveData.value = response
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    fun weatherInfo(key: String){
        disposable.add(
            weatherAPIService.getWeatherInfo(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherResponse>() {
                    override fun onSuccess(response: WeatherResponse) {
                        _weatherInfoLiveData.value = response
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }
}