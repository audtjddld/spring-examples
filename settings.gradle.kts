rootProject.name = "spring-examples"

includeProject(
    ":spring-blog-server" to "spring-blog-server",
    ":spring-blog-client" to "spring-blog-client",
    ":grpc" to "grpc"
)

// include(":spring-blog-server")
// include(":spring-blog-client")
// include(":grpc")
fun includeProject(vararg projects: Pair<String, String?>) {
    projects.forEach {
        val (moduleName, physicalPath) = it
        include(moduleName)

        physicalPath?.run {
            project(moduleName).projectDir = File(this)
        }
    }
}
