import { check } from "k6";
import http from "k6/http";

export let options = {
    minIterationDuration: "50ms",
    vus: 1,
    vusMax: 1,
    duration: "10s"
};

export default function() {
  let url = __ENV.url
  let res = http.get(`${url}`);
  check(res, {
      "is status 200": (r) => r.status === 200
  });
};