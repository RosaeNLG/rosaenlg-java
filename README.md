# RosaeNLG for Java

[RosaeNLG](https://rosaenlg.org) is a Natural Language Generation library.
This repo contains:
- a Java Wrapper (lib) around RosaeNLG
- a Java Server based on the Wrapper


## Documentation

- [RosaeNLG documentation](https://rosaenlg.org)
- [Wrapper JavaDoc](https://www.javadoc.io/doc/org.rosaenlg/java-wrapper/)
- [Server JavaDoc](https://www.javadoc.io/doc/org.rosaenlg/java-server/)


## Contrib

_this part of doc is just for me_

release process:
- update `CHANGELOG.md`
- in root `pom.xml` change `<revision>...</revision>` to proper version of RosaeNLG
- change `ROSAENLG_VERSION` in `.gitlab-ci.yml`
- `mvn clean` locally
- `mvn deploy` locally
- wait for [maven publication available](https://repo1.maven.org/maven2/org/rosaenlg/java-wrapper/)
- push for CI to generate docker file

misc:
- test packaging skipping tests: `mvn package -Dmaven.test.skip=true`
- test: `mvn -Dtest=TestApplicationPersistence test`

For the wrapper in case of issues with the download cache:
```xml
<overwrite>true</overwrite>
<skipCache>true</skipCache>
```
