////K6 tasks=====================================================================
val url = project.findProperty("urlArg")
tasks.register<Exec>("loadTest") {
    doFirst {
        checkNotNull(url) { "Please set URL to run load test against: -PurlArg=<your-url-here>" }
    }
    commandLine("bash", "-c", """docker run \
                                                |-i loadimpact/k6 run \
                                                |-e url=$url \
                                                |-< $rootDir/config/load-test/load-test-simple.js""".trimMargin())
}