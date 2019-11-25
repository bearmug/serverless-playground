plugins {
    groovy
}

val lombokVersion: String by project
val micronautVersion: String by project
dependencies {
    annotationProcessor(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    annotationProcessor("io.micronaut:micronaut-graal")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("io.micronaut:micronaut-inject-java")

    compileOnly("org.projectlombok:lombok:$lombokVersion")

    testCompile("io.micronaut:micronaut-http-server-netty")
    testImplementation("io.micronaut.test:micronaut-test-spock")
    testImplementation("io.micronaut:micronaut-inject-groovy")
    testImplementation("org.spockframework:spock-core") {
        exclude(group = "org.codehaus.groovy", module = "groovy-all")
    }
}

