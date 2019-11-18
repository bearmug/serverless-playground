import { check } from "k6";
import http from "k6/http";

export let options = {
    minIterationDuration: "10ms",
    vus: 1,
    vusMax: 1,
    duration: "10s",
    summaryTrendStats: ["min", "max", "p(50)", "p(90)", "p(95)", "p(99)"]
};

export default function() {
  let url = __ENV.url
  let res = http.get(`${url}`);
  check(res, {
      "is status 200": (r) => r.status === 200
  });
};