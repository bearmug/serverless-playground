#!/bin/bash
echo "AWS warm latency measurement init"
./gradlew awsLambdaDeploy
for url in \
  "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/nodejs" \
  "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/java" \
  "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/graal" \
  "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/kotlin" \
  "https://<aws-reference>.execute-api.eu-west-1.amazonaws.com/dev/ping/graal"
do
  echo "AWS warm latency measurement, url $url start"
  curl $url
  sleep 5
  for i in {1..10}
  do
    echo "AWS warm latency measurement, iteration $i url $url start"
    ./gradlew loadTest -PurlArg=$url
  done
done
echo "AWS cold start latency measurement, iteration $i done"
