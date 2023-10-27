package com.example.training.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


/**
 * it is not working with armenia.
 * because it is configured as an Armenian server
 */
@RestController
class HelloController {
    @GetMapping("api")
    fun hello(): String {
        return "hello it is response of spring framework"
    }
}
