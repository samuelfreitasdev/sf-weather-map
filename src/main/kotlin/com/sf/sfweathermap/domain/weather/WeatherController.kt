package com.sf.sfweathermap.domain.weather

import com.sf.sfweathermap.external.openweathermap.AppWeather
import com.sf.sfweathermap.external.openweathermap.OpenWeatherMapCachedService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/Weather")
class WeatherController(private val openWeatherMapService: OpenWeatherMapCachedService) {

    @ApiOperation(
        value = "Access current weather data for any location.",
        response = AppWeather::class
    )
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Return the current weather data."),
        ApiResponse(code = 500, message = "Return a fail dua a application internal erro.")
    ])
    @ResponseBody
    @GetMapping
    fun searchByCity(
        @ApiParam(
            value = "City name, state code and country code divided by comma, use ISO 3166 country codes.",
            example = "quixada,ce",
        )
        @RequestParam("city") city: String,
    ): Mono<ResponseEntity<AppWeather>> {
        return openWeatherMapService.queryByCity(city)
            .map { ResponseEntity.ok(it) }
            .doOnError { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(it.message) }
    }

}