package com.example.training.service

import com.example.training.grpc.blog.BlogPost
import com.example.training.grpc.blog.BlogServiceGrpcKt
import com.example.training.grpc.blog.GetBlogPostRequest
import com.example.training.log
import com.example.training.repository.BlogRepository
import org.springframework.stereotype.Service

@Service
class BlogService(
    private val blogRepository: BlogRepository
) : BlogServiceGrpcKt.BlogServiceCoroutineImplBase() {

    override suspend fun getBlogPost(request: GetBlogPostRequest): BlogPost {
        log.info {
            "received request: $request"
        }
        return blogRepository.findById(request.id)!!
    }
}
