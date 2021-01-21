package com.sf.sfweathermap.domain.about

import com.sf.sfweathermap.external.openweathermap.OpenWeatherMapServiceStatus
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.boot.actuate.health.Status
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/About")
class AboutController(private val openWeatherMapServiceStatus: OpenWeatherMapServiceStatus) {

    @ApiOperation(value = "Return the application current status")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Return the API current Status"),
        ApiResponse(code = 500, message = "Return a fail on check the API status.")
    ])
    @GetMapping
    @ResponseBody
    fun about(): Mono<About> {
        return openWeatherMapServiceStatus.health()
            .map {
                About(
                    healthy = it.status == Status.UP,
                    status = it.details.getOrDefault(OpenWeatherMapServiceStatus.MESSAGE, "Working...") as String
                )
            }
            .onErrorResume {
                Mono.just(
                    About(
                        healthy = false,
                        status = it.message.orEmpty()
                    )
                )
            }

    }

}