package com.example.training.config

import com.example.training.grpc.blog.BlogServiceGrpcKt.BlogServiceCoroutineStub
import com.linecorp.armeria.client.grpc.GrpcClients
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.server.tomcat.TomcatService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.apache.catalina.connector.Connector
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean

/**
 * this configuration is spring boot web configg
 */
//@Configuration
class AppConfig {
    fun getConnector(context: ServletWebServerApplicationContext): Connector {
        val webServer = context.webServer as TomcatWebServer
        webServer.start()
        return webServer.tomcat.connector
    }

    @Bean
    fun armeriaServerConfigurator(
        context: ServletWebServerApplicationContext
    ): ArmeriaServerConfigurator {

        val tomcatService = TomcatService.of(getConnector(context))


        // Customize the server using the given ServerBuilder. For example:
        return ArmeriaServerConfigurator {
            // Add DocService that enables you to send Thrift and gRPC requests from web browser.
            it.serviceUnder("/", tomcatService)

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
}
