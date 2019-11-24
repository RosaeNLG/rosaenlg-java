# Changelog

{::comment}
## [Unreleased]
### Added
### Fixed
### Changed
{:/comment}


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
