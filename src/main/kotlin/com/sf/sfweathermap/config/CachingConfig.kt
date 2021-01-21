package com.sf.sfweathermap.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
@EnableCaching
class CachingConfig {

    @Bean
    fun cacheManager(caffeine: Caffeine<*, *>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(caffeine as Caffeine<Any, Any>)
        return caffeineCacheManager
    }

    @Bean
    fun caffeineConfig(): Caffeine<*, *> {
        return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES)
    }

}