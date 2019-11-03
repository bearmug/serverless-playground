# Serverless playground
Here you can find several experiments with existing cloud providers infrastructure and their serverless 
features. Namely:
* [Experiment #1](doc/aws-lambda.md). Simple benchmarking for Java8/GraalVM -based AWS Lambdas and 
  their NodeJS competitor. 
  Java service created with [Micronaut](https://micronaut.io/), cloud delivery implemented with 
  [Serverless](https://serverless.com/) and load testing done with [k6](https://k6.io/). See cold/warm 
  [latency numbers](doc/aws-lambda.md#performance-samples) for client-side and AWS-side measurements.
* [Experiment #2](doc/gcp-cloud-run.md). Evaluates GCP Cloud Run in combination with GraalVM native images. 
  Again, [Micronaut](https://micronaut.io/) used as an initial Java framework. See latency numbers and stats
  attached.