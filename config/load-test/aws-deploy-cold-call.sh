#!/bin/bash
for i in {1..10}
do
   echo "AWS cold start latency measurement, iteration $i start"
   ./gradlew awsDeployAll
   sleep 10
   time echo "iteration $i measurement"
   for url in \
     "https://tn1cfyblg6.execute-api.eu-west-1.amazonaws.com/dev/ping/nodejs" \
     "https://tn1cfyblg6.execute-api.eu-west-1.amazonaws.com/dev/ping/java" \
     "https://tn1cfyblg6.execute-api.eu-west-1.amazonaws.com/dev/ping/graal" \
     "https://rz9c7ntq8a.execute-api.eu-west-1.amazonaws.com/dev/ping/kotlin" \
     "https://rz9c7ntq8a.execute-api.eu-west-1.amazonaws.com/dev/ping/graal"
   do
     echo "iteration $i loadTest for $url"
     ./gradlew loadTest -PurlArg=$url
   done
   echo "AWS cold start latency measurement, iteration $i done"
done