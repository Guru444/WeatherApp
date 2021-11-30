package com.kafein.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kafein.weatherapp.adapter.CityListAdapter
import com.kafein.weatherapp.adapter.LastSearchesAdapter
import com.kafein.weatherapp.databinding.ActivityMainBinding
import com.kafein.weatherapp.model.LastSearches
import com.kafein.weatherapp.model.response.CityListResponse
import com.kafein.weatherapp.util.MY_PERMISSIONS_REQUEST_LOCATION
import com.kafein.weatherapp.util.WeatherConstants
import com.kafein.weatherapp.util.checkLocationPermission
import com.kafein.weatherapp.util.requestLocationPermission
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.weather_bottom_sheet.view.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: CitySearchViewModel
    private lateinit var binding: ActivityMainBinding
    private val cityListAdapter = CityListAdapter()
    private val lastSearchesAdapter = LastSearchesAdapter()
    private var cityListTemp: ArrayList<CityListResponse.CityListResponseItem> = arrayListOf()
    var searchList: ArrayList<LastSearches> = arrayListOf()
    private var myLocationCityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Hawk.init(this).build()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        this.checkLocationPermission()


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
                myLocationCityName = it.localizedName.toString()
                it.key?.let { key -> viewModel.weatherInfo(key) }
            }
        })

        viewModel.weatherInfoLiveData.observe(this,{
            it.getOrNull(0)?.let {
                this.runOnUiThread {
                    weatherInfoBottomSheet(myLocationCityName, it.weatherText)
                }
            }
        })
    }

    private fun weatherInfoBottomSheet(cityName: String = "", weatherText: String?) {
        val view: View = layoutInflater.inflate(R.layout.weather_bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        view.tv_weather_title.text = view.context.getString(R.string.for_city_weather, cityName)
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

    private fun getLocationCity(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    val gcd = Geocoder(this, Locale.getDefault())
                    val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses.size > 0) {
                        myLocationCityName = addresses[0].adminArea.toString()
                        viewModel.searchCity(addresses[0].adminArea.toString())
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        getLocationCity()
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}