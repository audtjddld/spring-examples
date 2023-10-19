package com.example.training

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

val log = KotlinLogging.logger { }

@EnableWebFlux
@SpringBootApplication
class SpringWithArmeriaClientApplication

fun main(args: Array<String>) {

    runApplication<SpringWithArmeriaClientApplication>(*args)

    log.info { "Server has been started." }
}

