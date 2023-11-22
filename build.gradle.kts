plugins {
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.springboot)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.spring.dependencyManagement) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.jib) apply false
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        rootProject.libs.plugins.kotlin.spring
        rootProject.libs.plugins.spring.dependencyManagement
    }

    dependencies {
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
        // For example:
        jvmToolchain(21)
    }
}

tasks {
    jar {
        enabled = true
    }

    bootJar {
        enabled = false
    }
}
