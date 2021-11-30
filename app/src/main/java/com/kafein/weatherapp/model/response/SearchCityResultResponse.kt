package com.kafein.weatherapp.model.response


import com.google.gson.annotations.SerializedName

class SearchCityResultResponse : ArrayList<SearchCityResultResponse.SearchCityResultResponseItem>(){
    data class SearchCityResultResponseItem(
        @SerializedName("AdministrativeArea")
        var administrativeArea: AdministrativeArea? = null,
        @SerializedName("Country")
        var country: Country? = null,
        @SerializedName("DataSets")
        var dataSets: List<String?>? = null,
        @SerializedName("EnglishName")
        var englishName: String? = null,
        @SerializedName("GeoPosition")
        var geoPosition: GeoPosition? = null,
        @SerializedName("IsAlias")
        var isAlias: Boolean? = null,
        @SerializedName("Key")
        var key: String? = null,
        @SerializedName("LocalizedName")
        var localizedName: String? = null,
        @SerializedName("PrimaryPostalCode")
        var primaryPostalCode: String? = null,
        @SerializedName("Rank")
        var rank: Int? = null,
        @SerializedName("Region")
        var region: Region? = null,
        @SerializedName("SupplementalAdminAreas")
        var supplementalAdminAreas: List<Any?>? = null,
        @SerializedName("TimeZone")
        var timeZone: TimeZone? = null,
        @SerializedName("Type")
        var type: String? = null,
        @SerializedName("Version")
        var version: Int? = null
    ) {
        data class AdministrativeArea(
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
    
        data class Country(
            @SerializedName("EnglishName")
            var englishName: String? = null,
            @SerializedName("ID")
            var iD: String? = null,
            @SerializedName("LocalizedName")
            var localizedName: String? = null
        )
    
        data class GeoPosition(
            @SerializedName("Elevation")
            var elevation: Elevation? = null,
            @SerializedName("Latitude")
            var latitude: Double? = null,
            @SerializedName("Longitude")
            var longitude: Double? = null
        ) {
            data class Elevation(
                @SerializedName("Imperial")
                var imperial: Imperial? = null,
                @SerializedName("Metric")
                var metric: Metric? = null
            ) {
                data class Imperial(
                    @SerializedName("Unit")
                    var unit: String? = null,
                    @SerializedName("UnitType")
                    var unitType: Int? = null,
                    @SerializedName("Value")
                    var value: Int? = null
                )
    
                data class Metric(
                    @SerializedName("Unit")
                    var unit: String? = null,
                    @SerializedName("UnitType")
                    var unitType: Int? = null,
                    @SerializedName("Value")
                    var value: Int? = null
                )
            }
        }
    
        data class Region(
            @SerializedName("EnglishName")
            var englishName: String? = null,
            @SerializedName("ID")
            var iD: String? = null,
            @SerializedName("LocalizedName")
            var localizedName: String? = null
        )
    
        data class TimeZone(
            @SerializedName("Code")
            var code: String? = null,
            @SerializedName("GmtOffset")
            var gmtOffset: Int? = null,
            @SerializedName("IsDaylightSaving")
            var isDaylightSaving: Boolean? = null,
            @SerializedName("Name")
            var name: String? = null,
            @SerializedName("NextOffsetChange")
            var nextOffsetChange: Any? = null
        )
    }
}