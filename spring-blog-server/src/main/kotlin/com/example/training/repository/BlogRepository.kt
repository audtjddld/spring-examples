package com.example.training.repository

import com.example.training.grpc.blog.BlogPost

class BlogRepository(
    val data: Map<Long, BlogPost>
) {
    fun findById(id: Long): BlogPost? = data[id]
}
