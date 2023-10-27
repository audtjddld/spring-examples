package com.example.training.web

import com.example.training.grpc.blog.BlogServiceGrpcKt.BlogServiceCoroutineStub
import com.example.training.grpc.blog.CreateBlogPostRequest
import com.example.training.grpc.blog.GetBlogPostRequest
import com.example.training.log
import com.linecorp.armeria.common.HttpStatus
import java.time.LocalDateTime
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/blogs")
class BlogRestGrpcController(
    private val blogService: BlogServiceCoroutineStub
) {
    
    @GetMapping("{id}")
    suspend fun getBlog(
        @PathVariable("id") id: Long
    ): ResponseEntity<BlogResponse> {
        val blogPost =
            blogService.getBlogPost(
                GetBlogPostRequest.newBuilder()
                    .setId(id)
                    .build()
            )

        log.info {
            "received blogPost : $blogPost"
        }

        val blogResponse = BlogResponse(
            id = blogPost.id,
            title = blogPost.title,
            content = blogPost.content,
            createdAt = blogPost.createdAt,
            modifiedAt = blogPost.modifiedAt
        )

        return ResponseEntity.ok(blogResponse)
    }

    @PostMapping
    suspend fun createBlogs(): ResponseEntity<Void> {
        val createBlogPostRequest = CreateBlogPostRequest.newBuilder()
            .setContent("hello")
            .setTitle("new Blogs Post : ${LocalDateTime.now().toLocalDate()}")
            .build()
        blogService.createBlogPost(createBlogPostRequest)
        return ResponseEntity.status(
            HttpStatus.CREATED.code()
        ).build()
    }
}

data class BlogResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val modifiedAt: Long
)
