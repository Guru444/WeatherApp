package com.kafein.weatherapp.model.response


import com.google.gson.annotations.SerializedName

class WeatherResponse : ArrayList<WeatherResponse.WeatherResponseItem>(){
    data class WeatherResponseItem(
        @SerializedName("EpochTime")
        var epochTime: Int? = null,
        @SerializedName("HasPrecipitation")
        var hasPrecipitation: Boolean? = null,
        @SerializedName("IsDayTime")
        var isDayTime: Boolean? = null,
        @SerializedName("Link")
        var link: String? = null,
        @SerializedName("LocalObservationDateTime")
        var localObservationDateTime: String? = null,
        @SerializedName("MobileLink")
        var mobileLink: String? = null,
        @SerializedName("PrecipitationType")
        var precipitationType: Any? = null,
        @SerializedName("Temperature")
        var temperature: Temperature? = null,
        @SerializedName("WeatherIcon")
        var weatherIcon: Int? = null,
        @SerializedName("WeatherText")
        var weatherText: String? = null
    ) {
        data class Temperature(
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
                var value: Double? = null
            )
        }
    }
}