package com.example.training.web

import com.example.training.grpc.blog.BlogPost
import com.example.training.grpc.blog.BlogServiceGrpcKt.BlogServiceCoroutineStub
import com.example.training.grpc.blog.GetBlogPostRequest
import com.example.training.log
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/blogs")
class BlogV2Controller(
    private val stub: BlogServiceCoroutineStub
) {

    @GetMapping("{blogId}")
    suspend fun getBlogPost(@PathVariable("blogId") blogId: Long): BlogPost {
        val request = GetBlogPostRequest.newBuilder()
            .setId(blogId)
            .build()

        log.info {
            "sending request: $request"
        }

        return stub.getBlogPost(request)
    }
}
