package com.example.training.config

import com.example.training.grpc.blog.BlogServiceGrpcKt.BlogServiceCoroutineStub
import com.linecorp.armeria.client.grpc.GrpcClients
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppWebFluxConfig {

    @Bean
    fun armeriaServerConfigurator(
    ): ArmeriaServerConfigurator {


        // Customize the server using the given ServerBuilder. For example:
        return ArmeriaServerConfigurator {
            // Add DocService that enables you to send Thrift and gRPC requests from web browser.

            // Log every message which the server receives and responds.
            it.decorator(LoggingService.newDecorator())

            // Write access log after completing a request.
            it.accessLogWriter(AccessLogWriter.combined(), false)

            // Add an Armeria annotated HTTP service.
            // You can also bind asynchronous RPC services such as Thrift and gRPC:
            // it.service(THttpService.of(...));
            // it.service(GrpcService.builder()...build());
        }
    }

    @Bean
    fun grpcClient(): BlogServiceCoroutineStub {
        return GrpcClients.builder("http://localhost:8080")
            .build(BlogServiceCoroutineStub::class.java)
    }
//
//    @Bean
//    fun protobufHttpMessageConverter(): ProtobufHttpMessageConverter {
//        return ProtobufHttpMessageConverter()
//    }
}
