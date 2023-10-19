package com.example.training

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val log = KotlinLogging.logger { }

@SpringBootApplication
class SpringWithArmeriaServerApplication

fun main(args: Array<String>) {

    runApplication<SpringWithArmeriaServerApplication>(*args)

    log.info { "Server has been started." }
}

