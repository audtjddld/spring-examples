package com.example.training

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val log = KotlinLogging.logger { }

@SpringBootApplication
class BlogClientApplication

fun main(args: Array<String>) {
    runApplication<BlogClientApplication>(*args)
}
