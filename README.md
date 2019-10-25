# Serverless setup playground
This project is an experiment with serverless approach variances. Right now it includes cases like:
* **micronaut-java8 setup**
     [Micronaut](https://micronaut.io/) -based lambda, running with java8 [existing AWS runtime](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#lambda)
     and includes lambda function plus API gateway instance. Simple, but really slow on cold starts. Here is a
     [link](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#apiProxy) with an instruction how to generate 
     lambda from the scratch with micronaut tooling.
* **micronaut-graalvm setup**
    Micronaut-based lambda with [GraalVM](https://www.graalvm.org/docs/why-graal/) custom runtime. Cold start issue mitigated, 
    warm call timings similar to **java8**. [Here is Micronaut-based](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#_custom_graalvm_native_runtimes)
    build instruction.

## Pre-requisites
### Code deployment pipeline
[Serverless](https://serverless.com/) framework has been chosen as a cloud delivery vehicle. Please see 
[get-started](https://serverless.com/framework/docs/getting-started/) guide for setup details (few steps actually).

### AWS account
Experimentation implemented with AWS platform. There is an assumption that you have AWS account. Just in case, 
[here](https://aws.amazon.com/ru/premiumsupport/knowledge-center/create-and-activate-aws-account/) is AWS manual about it.

### AWS role
Serverless framework needs a set of permissions in order to deploy application stack. You may find full permissions list 
into attached [policy](./deployment-policy.json). This one has to be linked to AWS user. Remember to provide Serverless
with AWS user configuration, the simplest approach is to export keys variables:
```bash
export AWS_ACCESS_KEY_ID=<your access key here>
export AWS_SECRET_ACCESS_KEY=<your secret key>
```

## AWS Deployment
```bash
./gradlew serverlessDeploy # build artefacts and run deployment
./gradlew serverlessDeploy --dry-run # see the whole tasks dependency tree with this command
./gradlew serverlessRemove # remove all components from AWS
```

## Performance validation
Load test runs [simplified scenario](./load-test-simple.js) by default, which likeli fits to lambda [free
usage tier](https://aws.amazon.com/ru/lambda/pricing/). Otherwise, feel free to use alternative 
[scenario](./load-test-ramping.js). Default load test configuration could be triggered like:
```bash
./gradlew loadTest -PurlArg=https://xxx.execute-api.eu-west-1.amazonaws.com/playground/ping/graal
./gradlew loadTest -PurlArg=https://xxx.execute-api.eu-west-1.amazonaws.com/playground/ping/java
```
You can find URLs to test from serverless deployment command or from service info printout:
```bash
serverless info
```

## Logs review
```bash
./gradlew serverlessLogsGraal # request logs from Cloud Watch and see them into console
./gradlew serverlessLogsJava #
...
./gradlew sLG
./gradlew sLJ
```
