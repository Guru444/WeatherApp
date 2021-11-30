package com.kafein.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kafein.weatherapp.adapter.CityListAdapter
import com.kafein.weatherapp.adapter.LastSearchesAdapter
import com.kafein.weatherapp.databinding.ActivityMainBinding
import com.kafein.weatherapp.model.LastSearches
import com.kafein.weatherapp.model.response.CityListResponse
import com.kafein.weatherapp.util.WeatherConstants
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.weather_bottom_sheet.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CitySearchViewModel
    private lateinit var binding: ActivityMainBinding
    private val cityListAdapter = CityListAdapter()
    private val lastSearchesAdapter = LastSearchesAdapter()
    private var cityListTemp: ArrayList<CityListResponse.CityListResponseItem> = arrayListOf()
    var searchList: ArrayList<LastSearches> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Hawk.init(this).build()

        viewModel = ViewModelProvider(this).get(CitySearchViewModel::class.java)
        binding.progress.visibility = View.VISIBLE
        viewModel.cityList()

        observableLiveData()

        cityListAdapter.cityItemClickListener = { cityName->
            binding.progress.visibility = View.VISIBLE
            addToLastSearchList(cityName)
            viewModel.searchCity(cityName)
        }
        lastSearchesAdapter.lastSearchItemClickListener = { lastSearchName->
            binding.progress.visibility = View.VISIBLE
            viewModel.searchCity(lastSearchName)
        }

        binding.apply {
            rvLastSearches.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            rvLastSearches.adapter = lastSearchesAdapter

            checkLastSearchList()

            rvCityList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            rvCityList.adapter = cityListAdapter
            searchInput.doAfterTextChanged {
                it?.let {
                    if (cityListTemp.isNotEmpty()){
                        if (it.length >= 2) {
                            tvLastSearches.visibility = View.GONE
                            rvLastSearches.visibility = View.GONE
                            cityListAdapter.clearItems()
                            cityListAdapter.setItems(cityListTemp.filter {cityItem->  cityItem.localizedName?.lowercase()?.startsWith(it.toString()) == true } as ArrayList<CityListResponse.CityListResponseItem>)
                        }else{
                            tvLastSearches.visibility = View.VISIBLE
                            rvLastSearches.visibility = View.VISIBLE
                            cityListAdapter.clearItems()
                            checkLastSearchList()
                        }
                    }else{
                        searchInput.isEnabled = false
                    }
                }
            }
        }
    }

    private fun observableLiveData(){
        viewModel.cityListLiveData.observe(this, {
            it?.let {
                it.forEach {
                    cityListTemp.add(it)
                    binding.progress.visibility = View.GONE
                }
            }
        })

        viewModel.citySearchLiveData.observe(this, {
            it.getOrNull(0)?.let {
                it.key?.let { key -> viewModel.weatherInfo(key) }
            }
        })

        viewModel.weatherInfoLiveData.observe(this,{
            it.getOrNull(0)?.let {
                this.runOnUiThread {
                    weatherInfoBottomSheet(it.weatherText)
                }
            }
        })
    }

    private fun weatherInfoBottomSheet(weatherText: String?) {
        val view: View = layoutInflater.inflate(R.layout.weather_bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        view.tv_weather.text = weatherText
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
        binding.progress.visibility = View.GONE
    }

    private fun addToLastSearchList(searchTitle : String){
        val listLast = LastSearches()
        listLast.lastSearchTitle = searchTitle
        searchList.add(listLast)
        if(Hawk.get<ArrayList<LastSearches>>(WeatherConstants.LAST_SEARCH_LIST) != null) {
            if(Hawk.get<ArrayList<LastSearches>>(WeatherConstants.LAST_SEARCH_LIST).contains(listLast).not()) {
                if (searchList.size > 5){
                    searchList.removeAt(0)
                }
                Hawk.put(WeatherConstants.LAST_SEARCH_LIST, searchList)
            }
        }else{
            Hawk.put(WeatherConstants.LAST_SEARCH_LIST, searchList)
        }
    }

    private fun checkLastSearchList(){
        Hawk.get<ArrayList<LastSearches>>(WeatherConstants.LAST_SEARCH_LIST)?.let {
            binding.tvLastSearches.visibility = View.VISIBLE
            if (Hawk.get<ArrayList<LastSearches>>(WeatherConstants.LAST_SEARCH_LIST).isNotEmpty()) {
                searchList = Hawk.get(WeatherConstants.LAST_SEARCH_LIST)
                binding.rvLastSearches.visibility = View.VISIBLE
                lastSearchesAdapter.initializeValues(Hawk.get(WeatherConstants.LAST_SEARCH_LIST))
            }
        }
    }
}