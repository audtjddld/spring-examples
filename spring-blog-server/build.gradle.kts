plugins {
    alias(libs.plugins.gradle.git.properties)
    alias(libs.plugins.springboot)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.web)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    annotationProcessor(libs.spring.boot.annotation.configrator.processor)
    testImplementation(libs.spring.boot.test)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.logging.jvm)
    implementation(project(":grpc"))
    protobuf(project(":grpc"))

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.1")
    }
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

springBoot {
    buildInfo()
}

gitProperties {
    dateFormat = "yyyy-MM-dd'T'HH:mm:ssZz"
    dateFormatTimeZone = "Asia/Seoul"
}
