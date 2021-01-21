package com.sf.sfweathermap.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.sf.sfweathermap.domain"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "My OpenWeather API",
            "Search for weather situations in a city.",
            "BETA",
            "Terms of service",
            Contact("Samuel Freitas", "", "fresamuel@gmail.com"),
            "License of API", "API license URL", Collections.emptyList())
    }

}