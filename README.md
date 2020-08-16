# rate-limitter - library

### Assumptions Made:
1. Using Spring Basic authentication mechanism (SecurityConfig.java) to find the User/API name. And these users are initialized with help of InMemoryUserDetailsManager.
2. On a fixed interval (configurable), allowed api limits will be reset per api+user.

### Configurations:

rate-limiter.user-api-map.{user}[0].api => Name of API need for {user} to limit access.
rate-limiter.user-api-map.{user}[0].duration => Interval at which the limit resets.
rate-limiter.user-api-map.{user}[0].threshold => Maximum request allowed for the above configured duration.


default.user-api.duration => If User+API limit not pre configured, value configured here is the minimum duration before limits gets reset.
default.user-api.threshold => If User+API limit not pre configured, value configured here is the maximum request allowed for configured duration [default.user-api.duration].

#### If Properties default.user-api.duration and default.user-api.threshold not present, rate-limitter won't get configured for an application.

### Sample application.yaml configuration

default:
  user-api:
    duration: 120
    threshold: 60
rate-limiter:
  user-api-map:
    user1:
    - api: /v1/sample/second-api
      duration: 120
      threshold: 90
    - api: /v1/sample/first-api
      duration: 120
      threshold: 60
    user2:
    - api: /v1/sample/second-api
      duration: 120
      threshold: 60
    - api: /v1/sample/first-api
      duration: 120
      threshold: 120
