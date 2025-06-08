# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-06-08

### Added
- **🆕 XML Report Support**: Full JaCoCo XML report parsing with enhanced functionality
- **📊 Project Totals**: Display overall project coverage at the top of reports
- **📦 Package Summaries**: Hierarchical display with package-level aggregation  
- **⚡ Auto-Detection**: Automatically prefers XML over CSV reports for better features
- **🔧 New Configuration Options**:
  - `xmlReportPath`: Configure XML report file path
  - `showPackageSummary`: Control package summary display (default: true)
  - `-PjacocoXmlPath=...` for Gradle 4 XML path specification
  - `--xml-path=...` for Gradle 5+ XML path specification

### Changed
- **🚀 Default Report Format**: XML reports are now preferred over CSV for enhanced functionality
- **📈 Enhanced Output**: Hierarchical display showing PROJECT TOTAL → Package → Class structure
- **🔄 Backward Compatibility**: CSV support maintained but deprecated; lacks project totals
- Project property support for Gradle 4 compatibility
  - Use `-PjacocoClasses=...` for class filtering in Gradle 4
  - Use `-PjacocoCsvPath=...` for custom CSV path in Gradle 4 (deprecated)
  - Use `-PjacocoXmlPath=...` for custom XML path in Gradle 4
  - Maintains backward compatibility with `--classes` and `--csv-path` options in Gradle 5+

### Deprecated
- **CSV Report Path Options**: `csvReportPath`, `--csv-path`, and `-PjacocoCsvPath` are deprecated in favor of XML equivalents

### Improved
- Comprehensive XML parsing with DTD validation handling
- Better error messages and file detection
- Enhanced test coverage with 44 unit tests
- Code quality improvements with detekt and ktlint compliance

## [0.0.3] - 2025-06-05

### Changed
- Simplified configuration API for better Gradle 4 compatibility
  - Changed from `.set()` methods to direct assignment (`=`)
  - Example: `showTotal = true` instead of `showTotal.set(true)`

### Fixed
- Gradle 4 compatibility issues with extension configuration

## [0.0.2] - 2025-06-03

### Fixed
- Fixed compatibility issues with various Gradle versions

## [0.0.1] - 2025-06-02

### Added
- Initial release of JaCoCo Coverage Console Plugin
- Parse JaCoCo CSV reports and display coverage information in console table format
- Support for instruction coverage and branch coverage display
- Command-line options for class and CSV path specification
- Build configuration through Gradle extension
- Filtering support for specific classes and packages with wildcard matching
- Java 8+ compatibility
- Gradle 4.x - 8.x compatibility
- Comprehensive unit test suite with 100% test coverage
- Support for both Kotlin DSL and Groovy DSL in build scripts

### Features
- **Core Functionality**
  - CSV report parsing with robust error handling
  - Console table output with aligned formatting
  - Total coverage summary display
  
- **Filtering Options**
  - Exact class name matching
  - Package wildcard matching (`com.example.*`)
  - Multiple target specification
  - Command-line override support
  
- **Configuration**
  - Gradle extension for build script configuration
  - Command-line options for runtime customization
  - Default path resolution for JaCoCo reports
  
- **Compatibility**
  - Java 8+ runtime support
  - Gradle 4.x to 8.x compatibility
  - Cross-platform support (Windows, macOS, Linux)
