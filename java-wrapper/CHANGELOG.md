# Changelog

{::comment}
## [Unreleased]
### Added
### Fixed
### Changed
{:/comment}


## OLD VERSIONS ONLY - SEE MAIN CHANGELOG AT THE ROOT OF THE REPO


## [1.4.0] - 2019-11-07

### Changed

* switched to RosaeNLG 1.4.0
* doc reorganization


## [1.3.2] - 2019-11-01

### Fixed

* `runCompiled` did not work properly (did not pass the parameters to the template)
* `log4j` not included no more in the bundle
* added `compileFromJson` to compile a json package of templates, with schema validation
* used [this solution](https://github.com/oracle/graal/issues/1348) to solve issue when wrapper was embedded in a server

### Changed

* uses 1.3.2 version of RosaeNLG
* uses smaller js files per language, and not the 'fat js' RosaeNLG version no more
* `commons.io.FileUtils` to read files
* `runCompiled` didn't use `templateFunctionName` param so it was removed
* used JUnit 5
* added a logger


## [1.3.1] - 2019-10-20

* very first version with RosaeNLG 1.3.1
