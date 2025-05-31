# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-05-31

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