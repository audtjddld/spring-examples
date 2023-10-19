package com.example.training.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloSpringRestController {
    @GetMapping
    fun hello(): String {
        return "Hello, Spring Boot!"
    }

    @GetMapping("{number}")
    fun hello2(@PathVariable("number") number: Int): Number {
        return Number(number)
    }
}

data class Number(val value: Int)
