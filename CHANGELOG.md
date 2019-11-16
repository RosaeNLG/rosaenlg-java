# Changelog

{::comment}
## [Unreleased]
### Added
### Fixed
### Changed
{:/comment}



## [Unreleased]

### Added

* supports embedded resources for compile client

### Fixed

### Changed

* single repo for both Wrapper and Server
* clearer wrapper (thanks to Pierrick Hymbert)
* there was a route conflict for GET `/templates/reload` as reload could be the same of a template; to get the content of a template use `/templates/:templateId/template`
