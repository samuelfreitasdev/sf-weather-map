package com.sf.sfweathermap.external.openweathermap

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory
import io.micrometer.core.annotation.Counted
import io.micrometer.core.annotation.Timed
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


@Service
class OpenWeatherMapService(
    private val openWeatherMapApi: OpenWeatherMapApi
) {

    @Value("\${OPEN_WEATHER_MAP_KEY}")
    private lateinit var openWeatherMapKey: String

    @Timed("OPENWEATHERMAP-TIMER")
    @Counted("OPENWEATHERMAP-COUNT")
    fun queryByCity(city: String): Mono<AppWeather> {
        return openWeatherMapApi.queryByCity(
            query = city,
            apiKey = openWeatherMapKey,
            units = "metric",
            lang = "pt_br",
        ).flatMap {
            if (it.isSuccessful) {
                return@flatMap Mono.just(AppWeather(it.body()!!))
            }

            val responseError = Gson()
                .fromJson(
                    it.errorBody()!!.string(),
                    ResponseError::class.java
                )

            return@flatMap Mono.error(RuntimeException(responseError.message))
        }
    }
}

@Configuration
private class OpenWeatherMapApiService() {

    @Bean
    fun getApi(): OpenWeatherMapApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ReactorCallAdapterFactory.create())
            .build()

        return retrofit.create(OpenWeatherMapApi::class.java)
    }

}

interface OpenWeatherMapApi {

    @GET("/data/2.5/weather")
    fun queryByCity(
        @Query("q") query: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pt_br"
    ): Mono<Response<ApiResponseWeather>>

}