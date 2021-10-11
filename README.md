# Minimal jetty prefix bug reproduction

To start servers (jetty on 8080 and blaze on 8081) both serving the same routes:
```
sbt run
```

To test:
```
curl localhost:8080/pref-a/pref-b/test-resource
curl localhost:8081/pref-a/pref-b/test-resource
```

Expect both to return `Ok(should be found)`.

In reality, blaze appears to work correctly but jetty returns `InternalServerError(should not be found)`.
