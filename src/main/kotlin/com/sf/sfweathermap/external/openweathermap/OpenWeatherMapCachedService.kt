package com.sf.sfweathermap.external.openweathermap

import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class OpenWeatherMapCachedService(
    private val openWeatherMapService: OpenWeatherMapService,
    private val metrics: OpenWeatherMapMetrics,
    private final val circuitBreakerFactory: ReactiveResilience4JCircuitBreakerFactory,
) {

    private val circuitBreaker = circuitBreakerFactory.create("recommended")

    @Cacheable(value = ["OPEN_WEATHER_MAP"], key = "#city")
    fun queryByCity(city: String): Mono<AppWeather> {
        return metrics
            .registerQueryByCityDuration()
            .recordCallable {
                circuitBreaker.run {
                    openWeatherMapService.queryByCity(city)
                        .doOnSuccess { metrics.registerQueryByCity() }
                        .doOnError { metrics.registerQueryByCityFailed() }
                }
            }
    }

}