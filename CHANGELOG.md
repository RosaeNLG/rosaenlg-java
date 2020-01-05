# Changelog

{::comment}
## [Unreleased]
### Added
### Fixed
### Changed
{:/comment}

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
