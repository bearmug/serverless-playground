- [GCP Cloud Run (Beta) with GraalVM Native Images](#gcp-cloud-run--beta--with-graalvm-native-images)
  * [Runnable image](#runnable-image)
  * [Pre-requisites](#pre-requisites)
    + [GCP tooling and integrations](#gcp-tooling-and-integrations)
    + [Local docker setup](#local-docker-setup)
  * [Deployment](#deployment)
    + [Local experimentation](#local-experimentation)
    + [Cloud delivery](#cloud-delivery)
  * [Load testing](#load-testing)
    + [First runs after deployment](#first-runs-after-deployment)
    + [Attempts to catch "customer cold-starts"](#attempts-to-catch--customer-cold-starts-)
      - [Test #1](#test--1)
      - [Test #2](#test--2)
    + [Warm runs](#warm-runs)
    + [Performance samples](#performance-samples)
      - [Client-side deploy loop measurements](#client-side-deploy-loop-measurements)
      - [Client-side k6-based measurements](#client-side-k6-based-measurements)
      - [GCP side k6-based measurements](#gcp-side-k6-based-measurements)
    + [Performance measurement outcomes](#performance-measurement-outcomes)

# GCP Cloud Run (Beta) with GraalVM Native Images
This experiment is trying to touch [GCP Cloud Run](https://cloud.google.com/run/). The main question - 
does it worth efforts to run native images there? What about cold start timings? 

Cloud Run is somewhere in between [GCP Cloud Functions](https://cloud.google.com/functions/pricing-summary/)
and [Compute Engine](https://cloud.google.com/compute/), but with specifics like:
- virtually any language of choice and any tools could be used, since it is delivered as very well 
isolated unit, like Docker image.
- no money wasted for containers idle, customer pays for calls only. See [doc here](https://cloud.google.com/run/pricing).
- no operational pain with scaling, cluster maintenance, logs delivery and so on

## Runnable image
Experiment re-uses simplest [Micronaut GraalVM setup](https://github.com/micronaut-projects/micronaut-gcp/tree/master/examples/hello-world-cloud-run-graal).
We are not going to run builds at GCP infrastructure, but will delegate it to local docker
build and subsequent cloud deployment command.

## Pre-requisites
### GCP tooling and integrations
* Cloud deployment is done with `gcloud` CLI tool, see [setup start page](https://cloud.google.com/sdk/install)
  or [it's apt-get variance](https://cloud.google.com/sdk/docs/downloads-apt-get). 
* Your GCP project should have billing enabled. No worries, project load tests are supposed to 
  keep you within free tier, see **pricing** section [here](https://cloud.google.com/run/).
* Remember to put your GCP project id to [gradle.properties](../gradle.properties) file.  
* Please ensure you authorized Docker to [publish images](https://cloud.google.com/container-registry/docs/quickstart) 
  to GCP registry. Simple call `gcloud auth configure-docker` works for me.

### Local docker setup
Configured Docker tooling is absolutely required to run reproducible native image generation and related 
artifact publishing. [Here](https://docs.docker.com/install/linux/docker-ce/ubuntu/) you may find instruction,
recommended by GCP manuals. Also [here](https://github.com/bearmug/cfg-init/blob/master/cfg-tools-docker.sh)
is alternative script to do the same.

## Deployment
### Local experimentation
These commands could be issued to start/stop local service version:
```bash
./gradlew gcpStartLocal
./gradlew gcpStopLocal
```
Locally-started service could be challenged with requests like:
```bash
curl localhost:8080/ping/graal
```

### Cloud delivery
You can build and deploy artifact to GCP by single command:
```bash
./gradlew gcpDeploy
```
This one will build Docker image, publish it to GCP storage and roll out Cloud Run app. Deployment
to be finalized with target DNS name printout:
```bash                
...
... is serving 100 percent of traffic at https://gcp-serverless-graal-<id>.a.run.app

# you can test your service with command like this
curl https://gcp-serverless-graal-<id>.a.run.app/ping/graal
```
## Load testing
As an extra measures, this app has been configured with:
 - concurrency equal to 10 (default is 80) to be on low-throughput side deliberately
 - max instances number limited to 1, to measure single instance performance specifically.
[K6](https://k6.io/) load test iteration could be triggered with:
```bash
./gradlew loadTest -PurlArg=https://gcp-serverless-graal-<id>.a.run.app/ping/graal
```
Given [test](../config/load-test/load-test-simple.js) is just a repetitive call to the target URL during 
10 sec. If you interested about more scenarios, you can:
- play with alternative one [here](../config/load-test/load-test-ramping.js), use virtual users (VUS) ramping feature
- manipulate by application concurrency/max-instance/memory and other parameters, see documentation
with command `gcloud beta run deploy --help`.

### First runs after deployment
You can find out, that GCP is calling new service immediately after deployment. Even if service URL never triggered by
anyone outside of GCP.
Simply run deployment and watch for latency/call **Stackdriver** metrics for 5 minutes. This fenomena could be 
called "cold start" more or less. Those GCP internal calls have latency somewhere between 1000ms and 2000ms. Likely it 
is about initial calls, which include image download and it's startup.

Also, if interested, you may find k6 iterations log [here](../config/load-test/k6-gcp-cloud-run.log).

### Attempts to catch "customer cold-starts"
Worst user-bound timings are observed with load test right after service deployment. Any initial load test gives pretty 
same numbers. At the same time, this "cold-start" is totally different from 
[AWS lambda cold starts](aws-lambda.md#performance-samples), if we talk about initial calls.
#### Test #1
Lets just run **deployment -> cold start call** cycle x10 times:
```bash
# to be executed from project root
SERVICE_URL=https://gcp-serverless-graal-<id>.a.run.app ./config/load-test/gcp-deploy-cold-call.sh
```
#### Test #2
This is regular k6 run with project-default [test scenario](../config/load-test/load-test-simple.js).
Test issued after regular app deployment:
```bash
./gradlew loadTest -PurlArg=https://gcp-serverless-graal-<id>.a.run.app/ping/graal
```

### Warm runs
Once deployment and initial load test done, virtually any experiment to be finished with similar (and
quite optimistic stats, see below). It does not matter for how long we are waiting between iterations, 
10 minutes, 2 or 10 hours. 

### Performance samples
#### Client-side deploy loop measurements
10 sequential deployments literally killed app instance. First 3 iterations kept under 400ms, and then
response time gone up to 30 seconds. Looks like there is a rate limit for deployments per unit of time.

|               | min,ms | 50%-ile,ms | 90%-ile,ms | max,ms   | calls |
|---------------|--------|------------|------------|----------|-------|
| graal, cold   | 151    | 1653.5     | 32.95      | 33000    | 10    |

#### Client-side k6-based measurements
|               | min,ms | 50%-ile,ms | 90%-ile,ms | 95%-ile,ms | max,ms  | calls |
|---------------|--------|------------|------------|------------|---------|-------|
| graal, cold   | 40.33  | 46.42      | 53.77      | 64.71      | 1460    | 128   |
| graal, warm   | 38.17  | 41.16      | 47.67      | 51.39      | 71.05   | 149   |

#### GCP side k6-based measurements
CPU/memory billings captured from service metrics tab. See tabs `Container CPU Allocation` and
`Container Memory Allocation`. The only thing to change is a type of advanced aggregation, it is should be 
switched to `delta`, which will give proper per-minute costs aggregation. Costs per-call calculated
with pricing numberf from [here](https://cloud.google.com/run/pricing).

|              | 2%-ile,ms | 50%-ile,ms | 95%-ile,ms | 99%-ile,ms | CPU,s/s | mem,Gi/s | calls | cost/1mln calls|
|--------------|-----------|------------|------------|------------|---------|----------|-------|----------------|
| graal, cold  | 2.16      | 3.58       | 14.72      | 91.01      | 13.4    | 3        | 128   | 3.17421875     |
| graal, warm  | 2.12      | 3.2        | 7.69       | 14.01      | 14.9    | 4        | 149   | 2.867114094    |

### Performance measurement outcomes
It is quite hard to be sure 100%, since used tests and scenarios are limiting in a sense of statistics.
But looks like Cloud Run performs pretty well with native images (Except some edge cases!). Main points:
- user-based cold starts leaning towards GraalVM numbers at [AWS Lambda](./aws-lambda.md#performance-samples)
- deployment loop experimentation output looks suspicious. Still, CloudRun is a Beta GCP product
- single-instance processing throughput is better than AWS Lambda, but not much.
- billing approach looks optimistic, but costs per-call still higher than for AWS Lambdas,
  see lambdas pricing model [here](https://aws.amazon.com/lambda/pricing/) and experimentation data
  [here](./aws-lambda.md#performance-samples).
