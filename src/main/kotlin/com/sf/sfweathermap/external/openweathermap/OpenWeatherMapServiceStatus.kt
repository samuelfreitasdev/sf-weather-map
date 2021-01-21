package com.sf.sfweathermap.external.openweathermap

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class OpenWeatherMapServiceStatus(private val openWeatherMapService: OpenWeatherMapCachedService) : ReactiveHealthIndicator {

    override fun health(): Mono<Health> {
        try {
            return openWeatherMapService.queryByCity("Quixadá")
                .flatMap {
                    Mono.just(
                        Health
                            .Builder()
                            .up()
                            .build()
                    )
                }
                .onErrorResume { ex ->
                    Mono.just(
                        Health
                            .Builder()
                            .withDetail(MESSAGE, ex.message)
                            .down(ex)
                            .build()
                    )
                }
        } catch (ex: Throwable) {
            return Mono.just(
                Health
                    .Builder()
                    .withDetail(MESSAGE, ex.message)
                    .down(ex)
                    .build()
            )
        }
    }

    companion object {
        const val MESSAGE = "message"
    }
}