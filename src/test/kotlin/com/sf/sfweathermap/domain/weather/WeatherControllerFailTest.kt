package com.sf.sfweathermap.domain.weather

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(properties = ["OPEN_WEATHER_MAP_KEY=123"])
internal class WeatherControllerFailTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun testConnectionWithAFakeKey() {
        val city = "Quixadá"

        webTestClient
            .get()
            .uri {
                it
                    .path("/Weather")
                    .queryParam("city", city)
                    .build()
            }
            .exchange()
            .expectStatus().is5xxServerError
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
    }


}