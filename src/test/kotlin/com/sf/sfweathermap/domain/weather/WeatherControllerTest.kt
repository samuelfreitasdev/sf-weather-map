package com.sf.sfweathermap.domain.weather

import com.sf.sfweathermap.external.openweathermap.AppWeather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
internal class WeatherControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun testQueryByCity() {
        val city = "Quixadá"

        val weather = webTestClient
            .get()
            .uri {
                it
                    .path("/Weather")
                    .queryParam("city", city)
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody(AppWeather::class.java)
            .returnResult()
            .responseBody

        assertEquals(city, weather?.city.orEmpty())
        assertEquals("BR", weather?.country.orEmpty())
        assertNotNull(weather?.weather.orEmpty())
        assertNotNull(weather?.sunrise)
        assertNotNull(weather?.sunset)
        assertNotNull(weather?.wind?.deg)
        assertNotNull(weather?.wind?.speed)
    }


}