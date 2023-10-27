@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
}

java {
    sourceSets.getByName("main")
        .resources.srcDir("src/main/proto")
}

dependencies {
    api(libs.protobuf.util)
    api(libs.protobuf.kotlin)

    api(libs.grpc.protobuf)
    api(libs.grpc.stub)
    api(libs.grpc.netty.shaded)
    api(libs.grpc.kotlin.stub)
}
