plugins {
    alias(libs.plugins.gradle.git.properties)
    alias(libs.plugins.springboot)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.protobuf)
}

dependencies {
    runtimeOnly(libs.spring.boot.actuator)
    implementation(platform(libs.armeria.bom))

    implementation(libs.armeria.spring.tomcat10)
    implementation(libs.armeria.grpc)

    implementation(libs.armeria.spring.boot3.starter)
    implementation(libs.armeria.spring.boot3.webflux.starter)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)

    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    annotationProcessor(libs.spring.boot.annotation.configrator.processor)
    testImplementation(libs.spring.boot.test)

    implementation(libs.kotlin.logging.jvm)
    implementation(project(":grpc"))
    protobuf(project(":grpc"))

    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    // https://mvnrepository.com/artifact/org.hibernate/hibernate-validator
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpcKotlin.get()}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}
