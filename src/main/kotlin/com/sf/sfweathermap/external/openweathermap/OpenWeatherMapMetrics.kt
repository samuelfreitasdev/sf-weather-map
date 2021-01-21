package com.sf.sfweathermap.external.openweathermap

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class OpenWeatherMapMetrics(private val registry: MeterRegistry) {

    fun registerQueryByCity() {
        registry
            .counter("$PREFIX-QUERY-BY-CITY")
            .increment(1.0)
    }

    fun registerQueryByCityFailed() {
        registry.counter("$PREFIX-QUERY-BY-CITY-FAILED")
            .increment(1.0)
    }

    fun registerQueryByCityDuration() = registry.timer("$PREFIX-QUERY-BY-CITY-DURATION")

    companion object {
        const val PREFIX = "OPENWEATHERMAP"
    }

}