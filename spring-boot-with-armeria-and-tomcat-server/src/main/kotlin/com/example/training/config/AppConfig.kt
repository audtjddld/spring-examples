package com.example.training.config

import com.example.training.exception.GrpcExceptionHandler
import com.example.training.grpc.blog.BlogPost
import com.example.training.grpc.blog.BlogServiceGrpc
import com.example.training.service.BlogService
import com.example.training.service.HelloAnnotatedService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.healthcheck.HealthChecker
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.server.tomcat.TomcatService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.apache.catalina.connector.Connector
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfig {

    @Bean
    fun tomcatConnectorHealthChecker(context: ServletWebServerApplicationContext): HealthChecker {
        val connector = getConnector(context)
        return HealthChecker { connector.state.isAvailable }
    }

    fun getConnector(context: ServletWebServerApplicationContext): Connector {
        val webServer = context.webServer as TomcatWebServer
        webServer.start()
        return webServer.tomcat.connector
    }

    /**
     * A user can configure a [Server] by providing an [ArmeriaServerConfigurator] bean.
     */
    @Bean
    fun armeriaServerConfigurator(
        helloArmeriaClient: HelloAnnotatedService,
        context: ServletWebServerApplicationContext
    ): ArmeriaServerConfigurator {

        val tomcatService = TomcatService.of(getConnector(context))

        val exampleRequest = BlogPost.newBuilder()
            .setTitle("My first blog")
            .setContent("Hello Armeria!")
            .build()
        val docService = DocService.builder()
            .exampleRequests(
                BlogServiceGrpc.SERVICE_NAME,
                "CreateBlogPost", exampleRequest
            )
            .exclude(
                DocServiceFilter.ofServiceName(
                    ServerReflectionGrpc.SERVICE_NAME
                )
            )
            .build()

        // Customize the server using the given ServerBuilder. For example:
        return ArmeriaServerConfigurator {
            // Add DocService that enables you to send Thrift and gRPC requests from web browser.
            it.serviceUnder("/", tomcatService)
                .serviceUnder("/docs", docService)
                .decorator(LoggingService.newDecorator())
                .annotatedService("/", helloArmeriaClient)
                .service(
                    GrpcService.builder()
                        .addService(BlogService())
                        .enableUnframedRequests(true)
                        .exceptionMapping(GrpcExceptionHandler())
                        .build()
                )

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
    fun grpcService(): GrpcService {
        return GrpcService.builder()
            .addService(BlogService())
            .enableUnframedRequests(true)
            .exceptionMapping(GrpcExceptionHandler())
            .build()
    }
}
