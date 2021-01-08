<!--
Copyright 2020 Ludan Stoecklé
SPDX-License-Identifier: Apache-2.0
-->

# Java Wrapper - test project

This test project directly uses the RosaeNLG Java Wrapper.
Template files are put in resources folder (in subfolder `templates`).

- you have to install the Java wrapper and the parent POM before: in project root folder, run `mvn install -Dmaven.test.skip=true -Dgpg.skip` (GPG signature works when deploying only)
- package using maven
- run the `.class` file: `mvn exec:java`
- run the jar file: `java -jar target/rosaenlg-java-wrapper-user-0.0.1.jar`

