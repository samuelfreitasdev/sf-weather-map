package com.sf.sfweathermap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
@Controller
class Application {

    @GetMapping("/")
    fun goToSwagger(): String {
        return "redirect:/swagger-ui.html"
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
