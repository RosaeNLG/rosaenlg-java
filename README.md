# RosaeNLG for Java

[RosaeNLG](https://rosaenlg.org) is a Natural Language Generation library.
This repo contains:
- a Java Wrapper (lib) around RosaeNLG
- a Java Server based on the Wrapper


## RosaeNLG Java Wrapper

For documentation, see:
- [RosaeNLG documentation](https://rosaenlg.org)
- [Wrapper JavaDoc](https://www.javadoc.io/doc/org.rosaenlg/java-wrapper/)
- and [here](doc/modules/java-wrapper/java-wrapper.adoc)


## RosaeNLG Java Server

Java Server for RosaeNLG, based on the Wrapper.

For documentation, see:
- [RosaeNLG documentation](https://rosaenlg.org)
- [Server JavaDoc](https://www.javadoc.io/doc/org.rosaenlg/java-server/)
- and [here](doc/modules/java-server/java-server.adoc)


## Contrib

_this part of doc is just for me_

- test packaging skipping tests: `mvn package -Dmaven.test.skip=true`
- test: `mvn -Dtest=TestApplicationPersistence test`
- upload to central repo: `mvn deploy`

For the wrapper in case of issues with the download cache:
```xml
<overwrite>true</overwrite>
<skipCache>true</skipCache>
```
