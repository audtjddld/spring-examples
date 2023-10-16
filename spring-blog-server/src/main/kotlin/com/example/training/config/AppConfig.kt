package com.example.training.config

import com.example.training.grpc.blog.BlogPost
import com.example.training.repository.BlogRepository
import com.example.training.service.BlogService
import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter


@Configuration
class AppConfig : DisposableBean {
    private lateinit var server: Server

    @Bean
    fun blogRepository(): BlogRepository {
        val data = mapOf(
            1L to BlogPost.newBuilder()
                .setId(1)
                .setTitle("Armeria with Spring Boot")
                .setContent("Hello, Armeria with Spring Boot!")
                .build(),
            2L to BlogPost.newBuilder()
                .setId(2)
                .setTitle("Armeria with Kotlin")
                .setContent("Hello, Armeria with Kotlin!")
                .build()
        )
        return BlogRepository(data)
    }

    @Bean
    fun protobufHttpMessageConverter(): ProtobufHttpMessageConverter {
        return ProtobufHttpMessageConverter()
    }

    @Bean
    fun grpcServer(blogRepository: BlogRepository): Server {
        val server = ServerBuilder.forPort(6565)
            .addService(BlogService(blogRepository))
            .build()
        server.start()
        println("gRPC server started : $server")
        this.server = server
        return server
    }


    override fun destroy() {
        println("calling close() ${server}")
        server.awaitTermination()
    }
}
