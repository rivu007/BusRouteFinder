# Bus Route Finder

### Problem

We are adding a new bus provider to our system. In order to implement a very
specific requirement of this bus provider our system needs to be able to filter
direct connections. We have access to a weekly updated list of bus routes
in form of a **bus route data file**. As this provider has a lot of long bus
routes, we need to come up with a proper service to quickly answer if two given
stations are connected by a bus route.

## Getting it running

Clone this project. Use git clone https://github.com/rivu007/BusRouteFinder.git
Run `./gradlew clean build` at the root of the repository, where the `build.gradle` file is located.
That's it!

### REST API

Your micro service has to implement a REST-API supporting a single URL and only
GET requests. It has to serve
`http://localhost:8088/api/direct?dep_sid={}&arr_sid={}`. The parameters
`dep_sid` (departure) and `arr_sid` (arrival) are two station ids (sid)
represented by 32 bit integers.

The response has to be a single JSON Object:

```
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "dep_sid": {
      "type": "integer"
    },
    "arr_sid": {
      "type": "integer"
    },
    "direct_bus_route": {
      "type": "boolean"
    }
  },
  "required": [
    "dep_sid",
    "arr_sid",
    "direct_bus_route"
  ]
}
```

The `direct_bus_route` field has to be set to `true` if there exists a bus route
in the input data that connects the stations represented by `dep_sid` and
`arr_sid`. Otherwise `direct_bus_route` must be set to `false`.




### Example Data

Bus Routes Data File:
```
3
0 0 1 2 3 4
1 3 1 6 5
2 0 6 4
```

Query:
````
http://localhost:8088/api/direct?dep_sid=3&arr_sid=6
```

Response:
```
{
    "dep_sid": 3,
    "arr_sid": 6,
    "direct_bus_route": true
}
```


### Implementation

Please implement your solution in Java, preferably Java 8. We expect you to
demonstrate best practices for general software development. Feel free to use
helpful open source libraries if applicable. We will evaluate your source code
as well as the functionality and compliance of the application.

### Quick Smoke Test

*Note: This smoke test only checks for compliance, not for correctness!*

We will run some tests on your implementation, and because we are a friendly
bunch of developers, we share a (simplified) version of what we run. There 
are some bash scripts located in the `tests/` directory:
```
build_docker_image.sh
run_test_docker.sh
run_test_local.sh
simple_test.sh
```

Assuming a `bash` environment, you can do a quick local test:
```
bash build.sh
cd tests/
bash run_test_local.sh ../service.sh
```
This should output:
```
TEST PASSED!
```

Given a running `docker` installation and a UNIX-like environment you can run:
```
cd tests/
bash build_docker_image.sh YOUR_GIT_REPO_URL|ZIP_FILE
bash run_test_docker.sh
```
This should output:
```
TEST PASSED!
```



*Note: The docker based test assumes your running native docker. If not (e.g.
your on OSX) please adopt the `run_test_docker.sh` file and replace `localhost`
with the IP of your docker VM*
