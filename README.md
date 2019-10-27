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
into attached [policy](config/serverless/deployment-policy.json). This one has to be linked to AWS user. Remember to provide Serverless
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
Load test runs [simplified scenario](config/load-test/load-test-simple.js) by default, which likeli fits to lambda [free
usage tier](https://aws.amazon.com/ru/lambda/pricing/). Otherwise, feel free to use alternative 
[scenario](config/load-test/load-test-ramping.js). Default load test configuration could be triggered like:
```bash
./gradlew loadTest -PurlArg=https://xxx.execute-api.eu-west-1.amazonaws.com/playground/ping/graal
./gradlew loadTest -PurlArg=https://xxx.execute-api.eu-west-1.amazonaws.com/playground/ping/java
```
You can find URLs to test from serverless deployment command or from service info printout:
```bash
serverless info
```

AWS-side timings captured from CloudWatch Insights with query:
```bash
fields @timestamp, @duration, @billedDuration
| filter ispresent(@duration)
| stats count(),                # calls total number
        sum(@billedDuration),   # billed time, total
        sum(@duration),         # call time sum, total
        pct(@duration, 50),     # 50%-ile for calls timings
        pct(@duration, 95),     # 95%-ile
        pct(@duration, 99)      # 99%-ile 
by bin(5m)
```

### Performance samples
Simplest test [scenario](config/load-test/load-test-simple.js) with single virtual user and constant load gives numbers below
#### Client-side measurements, cold-start calls
```bash
http_req_duration..........: avg=200.77ms min=56.35ms  med=66.61ms  max=4.95s    p(90)=151.72ms p(95)=185.44ms #java
http_req_duration..........: avg=86.52ms  min=56.84ms  med=62.28ms  max=929.05ms p(90)=158.46ms p(95)=187.87ms #graal
```

#### AWS-side CloudWatch stats, cold-start calls
```bash
count=49  sum(@billedDuration)=7400ms  sum(@duration)=2738.16ms p(50)=2.16ms p(90)=14.17ms p(95)=39.01ms #java8
count=115 sum(@billedDuration)=12100ms sum(@duration)=1047.25ms p(50)=1.73ms p(90)=17.18ms p(95)=37.87ms #graal
```

#### Client-side measurements, warm calls
```bash
http_req_duration..........: avg=93.37ms  min=57.47ms  med=61.64ms  max=448.69ms p(90)=185.01ms p(95)=226.59ms #java8
http_req_duration..........: avg=81.28ms  min=56.59ms  med=61.6ms   max=351.74ms p(90)=152.94ms p(95)=206.24ms #graal
```

#### AWS-side CloudWatch stats, warm calls
```bash
count=108 sum(@billedDuration)=10800ms sum(@duration)=335.66ms p(50)=1.88ms p(90)=12.14ms p(95)=14.47ms #java8
count=122 sum(@billedDuration)=12300ms sum(@duration)=436.02ms p(50)=1.69ms p(90)=2.18ms  p(95)=12.62ms #graal
```

## Logs and stats review
```bash
./gradlew serverlessStatsGraal # request logs and stats from Cloud Watch and see them into console
./gradlew serverlessStatsJava #
...
./gradlew sSG
./gradlew sSJ
```
