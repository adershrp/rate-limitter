# rate-limitter - library

### Assumptions Made:
  - Using Spring Basic authentication mechanism (SecurityConfig.java) to find the User/API name. And these users are initialized with help of InMemoryUserDetailsManager.
  - On a fixed interval (configurable), allowed api limits will be reset per api+user.

### Configurations:

  #### User+API configurations
  * rate-limiter.user-api-map.{user}[0].api => Name of API need for {user} to limit access.
  * rate-limiter.user-api-map.{user}[0].duration => Interval in seconds at which the limit resets.
  * rate-limiter.user-api-map.{user}[0].threshold => Maximum request allowed for the above configured duration.

  #### Default Max Request per Duration configuration
  * default.user-api.duration => If User+API limit not pre configured, value configured here is the minimum duration in seconds before limits gets reset.
  * default.user-api.threshold => If User+API limit not pre configured, value configured here is the maximum request allowed for configured duration [default.user-api.duration].

#### If Properties default.user-api.duration and default.user-api.threshold not present, rate-limitter won't get configured for an application.

### Include lib using below dependency.
```xml
<dependency>
  <groupId>com.rate.limitter</groupId>
  <artifactId>rate-limitter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Configure the Default values, to enable the api access rate limiting.

If below values are not present, api-rate limiting won't get enabled.
```yml
default:
  user-api:
    duration: 120
    threshold: 60
```

### Configure User specific API limits
```yml
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
```
