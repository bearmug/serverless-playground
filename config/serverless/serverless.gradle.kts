tasks {
    //GCP tasks=====================================================================
    val gcpTag = "gcp-serverless-graal"
    register<Exec>("gcpAssemble") {
        group = "serverless"
        inputs.files(
                project.fileTree("src"),
                file("config/graal/Dockerfile-gcp-cloudrun"),
                file("build.gradle"))
        outputs.file("build/$gcpTag-version")
        commandLine("bash", "-c",
                "docker build . " +
                        "-t $gcpTag " +
                        "-t eu.gcr.io/gcp-serverless-257812/$gcpTag " +
                        "-f $rootDir/config/graal/Dockerfile-gcp-cloudrun && " +
                        "docker images -q $gcpTag > build/$gcpTag-version")
    }

    register<Exec>("gcpDeploy") {
        group = "serverless"
        dependsOn("gcpAssemble")
        commandLine("bash", "-c",
                "docker push eu.gcr.io/gcp-serverless-257812/$gcpTag && " +
                        "gcloud beta run deploy " +
                        "$gcpTag " +
                        "--platform=managed " +
                        "--allow-unauthenticated " +
                        "--region=europe-west1 " +
                        "--max-instances=1 " +
                        "--concurrency=10 " +
                        "--image=eu.gcr.io/gcp-serverless-257812/$gcpTag")
    }

    register<Exec>("gcpStartLocal") {
        group = "serverless"
        dependsOn("gcpAssemble")
        commandLine("bash", "-c",
                "docker run --rm --name $gcpTag -p 8080:8080 -d $gcpTag &&  docker ps")
    }

    register<Exec>("gcpStopLocal") {
        group = "serverless"
        commandLine("bash", "-c",
                "docker kill $gcpTag && docker ps")
    }

    ////AWS tasks=====================================================================
    register<Exec>("awsLambdaAssemble") {
        group = "serverless"
        inputs.files(
                project.fileTree("src"),
                file("config/graal/Dockerfile-aws-lambda"),
                file("build.gradle"))
        outputs.file("build/function.zip")
        val tag = "serverless-graal"
        commandLine("bash", "-c",
                "docker build . -t $tag -f $rootDir/config/graal/Dockerfile-aws-lambda && " +
                        "mkdir -p build && " +
                        "docker run --rm --entrypoint cat $tag  /home/application/function.zip > build/function.zip")
    }

    register<Exec>("awsLambdaDeploy") {
        group = "serverless"
        dependsOn("build", "awsLambdaAssemble")
        commandLine("bash", "-c", "serverless deploy")
    }

    register<Exec>("awsLambdaRemove") {
        group = "serverless"
        commandLine("bash", "-c", "serverless remove")
    }

    register<Exec>("awsLambdaStatsJava") {
        group = "serverless"
        val fn = "-f serverless-mn-java"
        commandLine("bash", "-c",
                "serverless logs $fn  && serverless info $fn && serverless metrics $fn")
    }

    register<Exec>("awsLambdaStatsGraal") {
        group = "serverless"
        val fn = "-f serverless-mn-graal"
        commandLine("bash", "-c",
                "serverless logs $fn  && serverless info $fn && serverless metrics $fn")
    }

    register<Exec>("awsLambdaStatsNode") {
        group = "serverless"
        val fn = "-f serverless-nodejs"
        commandLine("bash", "-c",
                "serverless logs $fn  && serverless info $fn && serverless metrics $fn")
    }

    ////K6 tasks=====================================================================
    val url = project.findProperty("urlArg")
    register<Exec>("loadTest") {
        doFirst {
            checkNotNull(url) { "Please set URL to run load test against: -PurlArg=<your-url-here>" }
        }
        commandLine("bash", "-c",
                "docker run -i loadimpact/k6 run -e url=$url -< $rootDir/config/load-test/load-test-simple.js")
    }
}