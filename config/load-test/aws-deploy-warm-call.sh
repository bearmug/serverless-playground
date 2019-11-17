#!/bin/bash
echo "AWS warm latency measurement init"
./gradlew awsDeployAll
for url in \
  "https://tn1cfyblg6.execute-api.eu-west-1.amazonaws.com/dev/ping/nodejs" \
  "https://tn1cfyblg6.execute-api.eu-west-1.amazonaws.com/dev/ping/java" \
  "https://tn1cfyblg6.execute-api.eu-west-1.amazonaws.com/dev/ping/graal" \
  "https://rz9c7ntq8a.execute-api.eu-west-1.amazonaws.com/dev/ping/kotlin" \
  "https://rz9c7ntq8a.execute-api.eu-west-1.amazonaws.com/dev/ping/graal"
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
