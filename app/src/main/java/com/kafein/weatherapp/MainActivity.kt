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

        cityListAdapter.cityItemClickListener = { cityName->
            binding.progress.visibility = View.VISIBLE
            viewModel.searchCity(cityName)
        }

        binding.apply {
            rvCityList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            rvCityList.adapter = cityListAdapter
            searchInput.doAfterTextChanged {
                it?.let {
                    if (cityListTemp.isNotEmpty()){
                        if (it.length >= 2) {
                            cityListAdapter.clearItems()
                            cityListAdapter.setItems(cityListTemp.filter {cityItem->  cityItem.localizedName?.lowercase()?.startsWith(it.toString()) == true } as ArrayList<CityListResponse.CityListResponseItem>)
                        }else{
                            cityListAdapter.clearItems()
                        }
                    }else{
                        searchInput.isEnabled = false
                    }
                }
            }
        }
    }
    fun weatherInfoBottomSheet(weatherText: String?) {
        val view: View = layoutInflater.inflate(R.layout.weather_bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        view.tv_weather.text = weatherText
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
        binding.progress.visibility = View.GONE
    }
}