package com.sf.sfweathermap.external.openweathermap

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import java.util.*

data class Clouds(
    @SerializedName("all") val all: Int
)

data class Coord(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)

data class Sys(
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int
)

data class Weather(
    @SerializedName("description") val description: String?
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val grndLevel: Int,
)

data class ApiResponseWeather(
    @SerializedName("id") val id: Int?,
    @SerializedName("message") val message: String,
    @SerializedName("cod") val cod: Int,
    @SerializedName("Coord") val coord: Coord?,
    @SerializedName("weather") val weathers: List<Weather?>?,
    @SerializedName("main") val main: Main?,
    @SerializedName("visibility") val visibility: Int?,
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("clouds") val clouds: Clouds?,
    @SerializedName("grnd_level") val grndLevel: Int?,
    @SerializedName("sys") val sys: Sys?,
    @SerializedName("name") val name: String?
)

@ApiModel(value = "Contains a weather from a city.")
data class AppWeather(
    @ApiModelProperty(value = "City name") @Expose @SerializedName("city") val city: String?,
    @ApiModelProperty(value = "Country name") @Expose @SerializedName("country") val country: String?,
    @ApiModelProperty(value = "City weather") @Expose @SerializedName("weather") val weather: String?,
    @ApiModelProperty(value = "Sunrise time in UTC") @Expose @SerializedName("sunrise") val sunrise: Date?,
    @ApiModelProperty(value = "Sunset time in UTC") @Expose @SerializedName("sunset") val sunset: Date?,
    @ApiModelProperty(value = "Related data about the weather") @Expose @SerializedName("main") val main: Main?,
    @ApiModelProperty(value = "Wind status") @Expose @SerializedName("wind") val wind: Wind?,
) {
    constructor(apiResponseWeather: ApiResponseWeather) : this(
        city = apiResponseWeather.name!!,
        weather = apiResponseWeather.weathers?.first()?.description.orEmpty(),
        country = apiResponseWeather.sys?.country.orEmpty(),
        sunrise = toDate(apiResponseWeather.sys?.sunrise),
        sunset = toDate(apiResponseWeather.sys?.sunset),
        main = apiResponseWeather.main!!,
        wind = apiResponseWeather.wind!!
    )
}

data class ResponseError(
    @Expose @SerializedName("cod") val code: Int,
    @Expose @SerializedName("message") val message: String,
)

private fun toDate(offset: Long?): Date? {
    return if (offset != null)
        Instant.now()
            .minusMillis(offset)
            .let { Date.from(it) }
    else null
}