<!--
Copyright 2020 Ludan Stoecklé
SPDX-License-Identifier: CC-BY-4.0
-->
# Security Policy

## Supported Versions

The latest version is the only supported.
When a security issue is found, it is corrected and a new patch version is published. The patch is only applied on the latest version, and is published along a new RosaeNLG main version.

[Addventa](https://www.addventa.com/), a company specialized in NLG and based in Paris provides commercial support on RosaeNLG, and can support defined versions in the long term.


## Tooling

- Most detected security issues come from third party libraries used in RosaeNLG. These libraries are followed up using [Snyk](https://snyk.io/).
- Sonar detects issues in RosaeNLG code itself, see [wrapper](https://sonarcloud.io/dashboard?id=java-wrapper) and [server](https://sonarcloud.io/dashboard?id=java-server).



## Reporting a Vulnerability

Security issues should be reported through [GitHub issue tracker](https://github.com/RosaeNLG/rosaenlg-java/issues). Use the "security" label.

Vulnerabilities that must remain private can be reported directly to the author: contact [at] rosaenlg [dot] org

Resolution timeframe depends on the severity and the complexity of the issue.
Usually, a new version containing third party dependencies fixes is published at least every month.

Also, feel free to submit PR correcting security issues.


## JVM / GraalVM Security

RosaeNLG js lib is run inside a Graal VM. The Graal VM brings [some security](https://www.graalvm.org/docs/security-guide/): _an embedder writes an application server (the host application) that runs JavaScript guest applications from a less trusted source_, knowing that `allowAllAccess` is not set using RosaeNLG: the js lib (and the templates) cannot access the JVM internals.

Pug templates cannot make any `require` so there is no access to `fs` (node.js filesystem lib) nor `http`.

