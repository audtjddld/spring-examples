import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("com.google.protobuf") version "0.9.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val protobufVersion = "3.24.4"
val grpcVersion = "1.58.0"
val grpcKotlinVersion = "1.4.0"
val coroutinesVersion = "1.7.3"
val kotlinLoggingVersion = "2.0.11"
val hibernateValidatorVersion = "8.0.1.Final"

dependencies {
    runtimeOnly("org.springframework.boot:spring-boot-starter-actuator")
    implementation(platform("com.linecorp.armeria:armeria-bom:1.25.2"))
    implementation("com.linecorp.armeria:armeria-spring-boot3-starter")
    implementation("com.linecorp.armeria:armeria-tomcat10")
    implementation("com.linecorp.armeria:armeria-grpc")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    protobuf(project(":grpc"))
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.1")
    }

    // https://mvnrepository.com/artifact/org.hibernate/hibernate-validator
    implementation("org.hibernate:hibernate-validator:$hibernateValidatorVersion")
}

dependencyManagement {
    imports {
        mavenBom("io.micrometer:micrometer-bom:1.11.1")
        mavenBom("io.netty:netty-bom:4.1.93.Final")
        mavenBom("com.linecorp.armeria:armeria-bom:1.25.2")
        mavenBom("io.github.resilience4j:resilience4j-bom:2.0.2")
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
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
