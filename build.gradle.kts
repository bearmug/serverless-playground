import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

apply(from = "$rootDir/config/serverless/load-test.gradle.kts")

subprojects {
    apply(from = "$rootDir/config/serverless/serverless.gradle.kts")
    apply(plugin = "java")
    apply(plugin = "application")
    apply(plugin = "com.github.johnrengelman.shadow")

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
        compileOnly("com.oracle.substratevm:svm")

        implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
        implementation("io.projectreactor:reactor-core")
        implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime") {
            exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-afterburner")
        }

        runtimeOnly("io.micronaut:micronaut-runtime")
        runtimeOnly("io.micronaut:micronaut-http-server-netty")
        runtimeOnly("ch.qos.logback:logback-classic:1.3.0-alpha4")
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
}

tasks {
    register("awsDeployAll") {
        group = "serverless"
        subprojects.forEach { subproject ->
            dependsOn(":${subproject.name}:awsLambdaDeploy")
        }
    }

    register("awsRemoveAll") {
        group = "serverless"
        subprojects.forEach { subproject ->
            dependsOn(":${subproject.name}:awsLambdaRemove")
        }
    }
}
