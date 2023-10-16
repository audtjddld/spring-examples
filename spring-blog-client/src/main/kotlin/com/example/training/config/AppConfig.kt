package com.example.training.config

import com.example.training.grpc.blog.BlogServiceGrpcKt.BlogServiceCoroutineStub
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter

@Configuration
class AppConfig {

    @Bean
    fun coroutineStub(): BlogServiceCoroutineStub {
        val channel = ManagedChannelBuilder.forAddress("localhost", 6565)
            .usePlaintext()
            .build()

        return BlogServiceCoroutineStub(channel)
    }

    @Bean
    fun protobufHttpMessageConverter(): ProtobufHttpMessageConverter {
        return ProtobufHttpMessageConverter()
    }
}

