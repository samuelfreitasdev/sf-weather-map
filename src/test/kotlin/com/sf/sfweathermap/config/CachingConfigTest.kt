package com.sf.sfweathermap.config

import com.github.benmanes.caffeine.cache.Cache
import com.sf.sfweathermap.external.openweathermap.OpenWeatherMapCachedService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.cache.CacheManager
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
@TestPropertySource(properties = ["OPEN_WEATHER_MAP_KEY=78ae5ea883e8aa4d843dd4bf7e471d3a"])
internal class CachingConfigTest {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @SpyBean
    private lateinit var openWeatherMapService: OpenWeatherMapCachedService

    @BeforeEach
    fun setup() {
        `clear all caches`()
    }

    @Test
    fun testCache() {
        `given a query for a city`()
        `given a query for a city`()
        `then should query only once at external service`()
        `then should has one item on cache manager`()
    }

    private fun `clear all caches`() {
        cacheManager.cacheNames
            .forEach { cacheManager.getCache(it)?.clear() }
    }

    private fun `given a query for a city`() {
        webTestClient
            .get()
            .uri {
                it
                    .path("/Weather")
                    .queryParam("city", "Quixadá")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
    }

    private fun `then should query only once at external service`() {
        verify(openWeatherMapService, times(1))
            .queryByCity(anyString())
    }

    private fun `then should has one item on cache manager`() {
        val cacheSize = (cacheManager.getCache("OPEN_WEATHER_MAP")
            !!.nativeCache as Cache<*, *>)
            .asMap()
            .keys
            .size

        Assertions.assertEquals(1, cacheSize)
    }

}