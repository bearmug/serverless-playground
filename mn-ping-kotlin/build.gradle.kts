import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.50"
}

val micronautVersion: String by project
val kotlinVersion: String by project
dependencies {
    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation("io.micronaut.test:micronaut-test-kotlintest")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

