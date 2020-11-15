# RosaeNLG for Java

[RosaeNLG](https://rosaenlg.org) is a Natural Language Generation library.
This repo contains:
- a Java Wrapper (lib) around RosaeNLG
- a Java Server based on the Wrapper


## Documentation

- [RosaeNLG documentation](https://rosaenlg.org)
- [Wrapper JavaDoc](https://www.javadoc.io/doc/org.rosaenlg/java-wrapper/)
- [Server JavaDoc](https://www.javadoc.io/doc/org.rosaenlg/java-server/)


## Release

_this part of doc is mostly for me_

- locally:
  - update `CHANGELOG.md`
  - in root `pom.xml` change `<revision>...</revision>` to proper version of RosaeNLG
  - change `ROSAENLG_VERSION` in `docker.yml`
  - `mvn clean`
  - `mvn package`
  - commit
  - create branch vXX.XX.XX
  - push branch
- publication will trigger on vXX.XX.XX branch thanks to GitHub Actions
- Docker images:
  - wait for [maven publication available](https://repo1.maven.org/maven2/org/rosaenlg/java-wrapper/)
  - trigger docker on GitHub Actions to generate docker images




## Maven tricks

misc:
- test packaging skipping tests: `mvn package -Dmaven.test.skip=true`
- test: `mvn -Dtest=TestApplicationPersistence test`

For the wrapper in case of issues with the download cache:
```xml
<overwrite>true</overwrite>
<skipCache>true</skipCache>
```


## Test server locally

in java-server folder:
- `mvn spring-boot:run`
- `http://localhost:8080/swagger-ui.html`
