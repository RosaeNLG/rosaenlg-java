<!--
Copyright 2020 Ludan Stoecklé
SPDX-License-Identifier: Apache-2.0
-->
# Contributing to RosaeNLG, Java version

## Welcomed Contributions

Contributions of any kind are welcomed: code, ideas, etc.

You may start by improving the quality:
- debug
- code quality improvements

Please check [contribution on RosaeNLG, main repo](https://github.com/RosaeNLG/rosaenlg/blob/master/CONTRIBUTING.md).


## Contribution Process

Ideation:

- create [an issue on GitHub](https://github.com/RosaeNLG/rosaenlg-java/issues): bug, idea, etc.
- exchange with the other: what should be done, best approach etc.

Development:

- create a feature branch; name it after the topic: *my-feature* or *issue-#123*
- develop
- write tests
- lint your code following current configuration
- each commit **MUST** contain a sign off message (see below)
- new contents must be under Apache 2.0 license
- update `CHANGELOG.md` (leave `## [Unreleased]`)
- push your branch
- check that github actions is green
- check Sonar dashboard on [wrapper](https://sonarcloud.io/dashboard?id=java-wrapper) and on [server](https://sonarcloud.io/dashboard?id=java-server), correct everything
- when done, create a PR
- once accepted, code gets merged in master

Documentation:

- **please update the technical documentation along your developments: you have to make another PR**
- the end user documentation for Java version is managed in the [main RosaeNLG repo](https://github.com/RosaeNLG/rosaenlg): check files `java-wrapper.adoc` and `java-server.adoc`
- please update them and make a separate PR on that repo

Publish:

- a new version is built by the core maintainers and integrate the changes in master: see [Versioning, Releases](README.md#versioning-releases)


## Sign off

For compliance purposes, [Developer Certificate of Origin (DCO) on Pull Requests](https://github.com/apps/dco) is activated on the repo.

In practice, you must add a `Signed-off-by:` message at the end of every commit:
```
This is my commit message

Signed-off-by: Random J Developer <random@developer.example.org>
```

Add `-s` flag to add it automatically: `git commit -s -m 'This is my commit message'`.


## License

RosaeNLG is released under Apache 2.0 license.
**New code must be release under Apache 2.0.**

Each file of code **must** contain, in a comment at the top, the license, copyright and author information.
It is automatically added in all new `.java` files when not present. You can modify it (typically update copyright) before commiting.
