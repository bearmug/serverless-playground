import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    groovy
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}
apply(from = "config/serverless/serverless.gradle.kts")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

version = "0.1"
group = "bearmug.lambda"

repositories {
    mavenCentral()
    maven(url = "https://jcenter.bintray.com")
}

val micronautVersion: String by project
dependencies {
    annotationProcessor(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    annotationProcessor("io.micronaut:micronaut-graal")
    annotationProcessor("org.projectlombok:lombok:1.18.10")
    annotationProcessor("io.micronaut:micronaut-inject-java")
    compileOnly("com.oracle.substratevm:svm")
    compileOnly("org.projectlombok:lombok:1.18.10")
    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.projectreactor:reactor-core")
    implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime") {
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-afterburner")
    }
    runtimeOnly("io.micronaut:micronaut-http-server-netty")
    runtimeOnly("ch.qos.logback:logback-classic:1.3.0-alpha4")
    testCompile("io.micronaut:micronaut-http-server-netty")
    testImplementation("io.micronaut.test:micronaut-test-spock")
    testImplementation("io.micronaut:micronaut-inject-groovy")
    testImplementation("org.spockframework:spock-core") {
        exclude(group = "org.codehaus.groovy", module = "groovy-all")
    }
}

application {
    mainClassName = "bearmug.lambda.ApplicationGCP"
}

tasks {
    register<Zip>("buildZip") {
        from(sourceSets.main.get().output)
        into("lib") { from(
            configurations.compileClasspath,
            configurations.runtimeClasspath)
        }
    }

    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
        dependsOn += "buildZip"
    }

    named<JavaExec>("run") {
        doFirst {
            jvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
        }
    }

    withType<JavaCompile>{
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }
}

