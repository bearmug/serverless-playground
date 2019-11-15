import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    groovy
    application
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.50"
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
val kotlinVersion: String by project
dependencies {
    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    compileOnly("com.oracle.substratevm:svm")

    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("io.projectreactor:reactor-core")
    implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime") {
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-afterburner")
    }

    runtimeOnly("io.micronaut:micronaut-runtime")
    runtimeOnly("io.micronaut:micronaut-http-server-netty")
    runtimeOnly("ch.qos.logback:logback-classic:1.3.0-alpha4")

    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation("io.micronaut.test:micronaut-test-spock")
    testImplementation("io.micronaut:micronaut-inject-groovy")
    testImplementation("org.spockframework:spock-core") {
        exclude(group = "org.codehaus.groovy", module = "groovy-all")
    }
}

application {
    mainClassName = "bearmug.lambda.Application"
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

