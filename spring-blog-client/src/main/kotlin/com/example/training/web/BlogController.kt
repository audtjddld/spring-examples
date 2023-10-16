package com.example.training.web

import com.example.training.grpc.blog.BlogPost
import com.example.training.grpc.blog.BlogServiceGrpcKt.BlogServiceCoroutineStub
import com.example.training.grpc.blog.GetBlogPostRequest
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/blogs")
class BlogController(
    private val stub: BlogServiceCoroutineStub
) {

    @GetMapping("{blogId}")
    fun getBlogPost(@PathVariable("blogId") blogId: Long): BlogPost {
        val request = GetBlogPostRequest.newBuilder()
            .setId(blogId)
            .build()
        println("sending request: $request")

        return runBlocking { stub.getBlogPost(request) }
    }
}
