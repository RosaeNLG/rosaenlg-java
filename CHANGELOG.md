# Changelog

{::comment}
## [Unreleased]
### Added
### Fixed
### Changed
{:/comment}

## [1.20.0] 2020-11-13

### Changed

* uses RosaeNLG 1.20.0


## [1.19.0] 2020-11-02

### Added

* now supports `outputData` feature

### Fixed

* outdated dependencies update
* updated JSON RosaeNLG package format, now same as node.js version (sources in `src`)

### Changed

* uses RosaeNLG 1.19.0


## [1.18.1] 2020-10-30

### Changed

* uses RosaeNLG 1.18.1
* released from Github to docker hub

## [1.18.0] 2020-10-23

### Changed

* uses RosaeNLG 1.18.0
* license change, now Apache 2.0

## [1.16.3] 2020-07-04

### Changed

* uses RosaeNLG 1.16.3


## [1.16.2] 2020-07-04

### Changed

* uses RosaeNLG 1.16.2


## [1.16.1] 2020-06-30

### Changed

* uses RosaeNLG 1.16.1
* uses `org.everit.json.schema` 1.3.0 to validate JSON schemas


## [1.16.0] 2020-06-12

### Changed

* uses RosaeNLG 1.16.0


## [1.15.1] 2020-05-09

### Changed

* uses RosaeNLG 1.15.1 which includes Spanish support


## [1.13.0] 2020-03-30

### Changed

* uses RosaeNLG 1.13.0


## [1.8.2] 2020-02-05

### Changed

* uses RosaeNLG 1.8.2


## [1.6.1] 2020-01-11

### Changed

* uses RosaeNLG 1.6.1


## [1.5.8] 2020-01-06

### Changed

* uses RosaeNLG 1.5.8


## [1.5.7] 2020-01-05

### Changed

* uses RosaeNLG 1.5.7


## [1.5.6] 2019-14-12

### Changed

* uses RosaeNLG 1.5.6 (NB: 1.5.5 was not released)
* removed `disableFiltering` option


## [1.5.4] 2019-12-04

### Changed

* uses RosaeNLG 1.5.4


## [1.5.3] 2019-11-24

### Changed

* uses RosaeNLG 1.5.3


## [1.5.2] 2019-11-19

### Added

* supports embedded resources for compile client

### Fixed

### Changed

* single repo for both Wrapper and Server
* Docker image is also built from here
* clearer wrapper (thanks to Pierrick Hymbert)
* there was a route conflict for GET `/templates/reload` as reload could be the same of a template; to get the content of a template use `/templates/:templateId/template`
