# Serverless setup playground
This project is an experiment with serverless approach variances. Right now it includes cases like:
* **micronaut-java8 setup**
    [Micronaut]()-based lambda, could be used with java8 [existing AWS runtime](). Simple, but really
    slow on cold starts.
* **micronaut-graalvm setup**
    [Micronaut]()-based lambda with [GraalVM]() custom runtime. Cold start issue mitigated, still
    could be not a perfect match for some cases.

## Pre-requisites
### Code deployment pipeline
### AWS account
### AWS role

## Artifacts build
```bash
./gradlew graalAssemble
```

## AWS Deployment
```bash
./gradlew serverlessDeploy
./gradlew serverlessRemove
```

## Performance validation
```bash
./gradlew loadTest -PurlArg=https://xxx.execute-api.eu-west-1.amazonaws.com/playground/ping/graal
./gradlew loadTest -PurlArg=https://xxx.execute-api.eu-west-1.amazonaws.com/playground/ping/java
```

```bash
./gradlew serverlessLogsGraal
./gradlew serverlessLogsJava
...
./gradlew sLG
./gradlew sLJ
```
