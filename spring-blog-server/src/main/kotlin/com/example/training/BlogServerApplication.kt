package com.example.training

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val log = KotlinLogging.logger {}

@SpringBootApplication
class BlogServerApplication

fun main(args: Array<String>) {
    runApplication<BlogServerApplication>(*args)
}
