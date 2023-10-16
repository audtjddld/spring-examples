package com.example.training

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogClientApplication

fun main(args: Array<String>) {
    runApplication<BlogClientApplication>(*args)
}
