package com.kafein.weatherapp.model.response


import com.google.gson.annotations.SerializedName

class CityListResponse : ArrayList<CityListResponse.CityListResponseItem>(){
    data class CityListResponseItem(
        @SerializedName("CountryID")
        var countryID: String? = null,
        @SerializedName("EnglishName")
        var englishName: String? = null,
        @SerializedName("EnglishType")
        var englishType: String? = null,
        @SerializedName("ID")
        var iD: String? = null,
        @SerializedName("Level")
        var level: Int? = null,
        @SerializedName("LocalizedName")
        var localizedName: String? = null,
        @SerializedName("LocalizedType")
        var localizedType: String? = null
    )
}