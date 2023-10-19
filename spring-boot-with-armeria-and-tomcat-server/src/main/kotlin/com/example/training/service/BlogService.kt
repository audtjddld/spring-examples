package com.example.training.service

import com.example.training.exception.BlogNotFoundException
import com.example.training.grpc.blog.BlogPost
import com.example.training.grpc.blog.BlogServiceGrpcKt
import com.example.training.grpc.blog.CreateBlogPostRequest
import com.example.training.grpc.blog.DeleteBlogPostRequest
import com.example.training.grpc.blog.GetBlogPostRequest
import com.example.training.grpc.blog.ListBlogPostsRequest
import com.example.training.grpc.blog.ListBlogPostsResponse
import com.example.training.grpc.blog.UpdateBlogPostRequest
import com.example.training.log
import com.google.protobuf.Empty
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


class BlogService : BlogServiceGrpcKt.BlogServiceCoroutineImplBase() {

    private val idGenerator = AtomicLong()
    private val blogPosts: MutableMap<Long, BlogPost> = ConcurrentHashMap()

    override suspend fun createBlogPost(request: CreateBlogPostRequest): BlogPost {
        val id = idGenerator.getAndIncrement()
        val now = Instant.now()
        val updated = BlogPost.newBuilder()
            .setId(id)
            .setTitle(request.getTitle())
            .setContent(request.getContent())
            .setModifiedAt(now.toEpochMilli())
            .setCreatedAt(now.toEpochMilli())
            .build()
        blogPosts.put(id, updated)

        return updated
    }

    override suspend fun getBlogPost(request: GetBlogPostRequest): BlogPost {
        val blogPost = blogPosts.get(request.id)
        log.info {
            "blogPost : $blogPost"
        }
        return blogPost
            ?: throw BlogNotFoundException("The blog post does not exist. ID: " + request.id)
    }

    override suspend fun listBlogPosts(request: ListBlogPostsRequest): ListBlogPostsResponse {
        return ListBlogPostsResponse.newBuilder()
            .addAllBlogs(blogPosts.values)
            .build()
    }

    override suspend fun updateBlogPost(request: UpdateBlogPostRequest): BlogPost {
        val oldBlog = blogPosts.get(request.id)
            ?: throw BlogNotFoundException("The blog post does not exist. ID: " + request.id)
        val newBlogPost = oldBlog.toBuilder()
            .setTitle(request.title)
            .setContent(request.content)
            .setModifiedAt(Instant.now().toEpochMilli())
            .build()
        blogPosts.put(request.id, newBlogPost)
        return newBlogPost
    }

    override suspend fun deleteBlogPost(request: DeleteBlogPostRequest): Empty {
        blogPosts.get(request.id) ?: throw BlogNotFoundException("The blog post does not exist. ID: " + request.id)
        return Empty.getDefaultInstance()
    }
}
