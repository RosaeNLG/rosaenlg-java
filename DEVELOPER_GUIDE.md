<!--
Copyright 2020 Ludan Stoecklé
SPDX-License-Identifier: CC-BY-4.0
-->
# Developer Guide

Read this if you want to contribute to RosaeNLG Java version.


## Architecture

RosaeNLG is a TypeScript / JavaScript project, not a Java project.

RosaeNLG Java version is not a Java rewrite, but a wrapper around the main version. It is run within a JVM thanks to [GraalJS](http://www.graalvm.org/docs/reference-manual/languages/js/).


## Code, Tooling

Code is on Github:

- [Java version](https://github.com/RosaeNLG/rosaenlg-java)
- [RosaeNLG project group](https://github.com/RosaeNLG)
- Sonar dashboards:
  - [wrapper](https://sonarcloud.io/dashboard?id=java-wrapper)
  - [server](https://sonarcloud.io/dashboard?id=java-server)


## Main Project Structure

Mono repo containing 2 java projects:

- `java-wrapper`: a Java library around RosaeNLG. It run RosaeNLG, which is a JavaScript library, in a JVM thanks to [GraalJS](http://www.graalvm.org/docs/reference-manual/languages/js/). The wrapper artefact is a `.jar` file.
- `java-server`: a Java server using the wrapper. It generates a `.war` file, which can run in a plain Java Servlet server like Tomcat, or directly using embedded server.


## API

Java API is aligned with the node.js server API. Please check `openApiDocumentation.json` in [RosaeNLG main project](https://github.com/RosaeNLG/rosaenlg).


## Build and Test

Checkout [RosaeNLG main repo](https://github.com/RosaeNLG/rosaenlg-java).

The project uses Maven.

Bundled RosaeNLG JavaScript files are not kept in the repo. Maven will download the proper version in `java-wrapper/src/main/resources`.


### Maven tricks

For the wrapper in case of issues with the download cache:
```xml
<overwrite>true</overwrite>
<skipCache>true</skipCache>
```

Misc:
- test packaging skipping tests: `mvn package -Dmaven.test.skip=true`
- test: `mvn -Dtest=TestApplicationPersistence test`


### Test Java Server Locally

In `java-server` folder:
- `mvn spring-boot:run`
- access the [local swagger](http://localhost:8080/swagger-ui.html)


## Publish a new version

_for developers who can do the release_

- locally:
  - update `CHANGELOG.md`
  - in root `pom.xml` change `<revision>...</revision>` to proper version of RosaeNLG
  - change `ROSAENLG_VERSION` in `docker.yml` (same number)
  - `mvn clean`
  - `mvn package`
  - commit
  - create branch vXX.XX.XX
  - push branch
- publication will trigger on vXX.XX.XX branch thanks to GitHub Actions
- Docker images:
  - **wait** for [maven publication available](https://repo1.maven.org/maven2/org/rosaenlg/java-wrapper/)
  - trigger docker on GitHub Actions to generate docker images 


## Expired key for Maven

* check if key has expired: `gpg --list-keys`
* generate a new key with a new passphrase
* export the new key: `gpg --output private.pgp --armor --export-secret-key username@email`
* on GitHub, update secrets: MAVEN_GPG_PRIVATE_KEY and MAVEN_GPG_PASSPHRASE
* make the keys public: `gpg --keyserver keyserver.ubuntu.com --send-keys KEY_ID`, check with `gpg --keyserver keyserver.ubuntu.com --recv-key KEY_ID`
