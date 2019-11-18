#!/bin/bash
for i in {1..10}
do
   echo "AWS cold start latency measurement, iteration $i start"
   ./gradlew awsLambdaDeploy
   sleep 10
   time echo "iteration $i measurement"
   for url in \
     "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/nodejs" \
     "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/java" \
     "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/graal" \
     "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/kotlin" \
     "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/graal"
   do
     echo "iteration $i loadTest for $url"
     ./gradlew loadTest -PurlArg=$url
   done
   echo "AWS cold start latency measurement, iteration $i done"
done