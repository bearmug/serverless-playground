#!/bin/bash
for i in {1..10}
do
   echo "GCP cold start latency measurement, iteration $i start"
   ./gradlew gcpDeploy
   sleep 60
   time echo "iteration $i measurement"
   time curl $SERVICE_URL/ping/graal
   time echo "iteration $i measurement done"
   sleep 60
   echo "GCP cold start latency measurement, iteration $i done"
done