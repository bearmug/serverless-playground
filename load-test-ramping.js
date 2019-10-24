import { check } from "k6";
import http from "k6/http";

export let options = {
    vus: 0,
    stages: [
        { duration: "10s", target: 300 },
        { duration: "10s", target: 300 },
        { duration: "10s", target: 0 },
    ]
};

export default function() {
  let url = __ENV.url
  let res = http.get(`${url}`);
  check(res, {
      "is status 200": (r) => r.status === 200
  });
};