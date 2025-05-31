# JaCoCo Coverage Console Plugin

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.mas0061.jacoco-coverage-console?logo=gradle)](https://plugins.gradle.org/plugin/io.github.mas0061.jacoco-coverage-console)
[![CI](https://github.com/mas0061/jacoco-coverage-console-plugin/workflows/CI/badge.svg)](https://github.com/mas0061/jacoco-coverage-console-plugin/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A Gradle plugin that parses JaCoCo CSV reports and displays coverage information in a console table format.

## ✨ Features

- 📊 Parse JaCoCo CSV reports with Instruction/Branch coverage display
- 🎯 Show total coverage or filter by specific classes/packages
- ⚙️ Configuration via build.gradle or command-line options
- 🔧 Compatible with Gradle 4-8 and Java 8+
- 🔍 Wildcard filtering for packages and classes

## 🚀 Quick Start

### Installation

**Kotlin DSL (build.gradle.kts):**
```kotlin
plugins {
    id("io.github.mas0061.jacoco-coverage-console") version "0.0.1"
}
```

**Groovy DSL (build.gradle):**
```groovy
plugins {
    id 'io.github.mas0061.jacoco-coverage-console' version '0.0.1'
}
```

### Basic Usage
```bash
# 1. Ensure JaCoCo CSV reports are enabled
# 2. Run tests with coverage
./gradlew test jacocoTestReport jacocoCoverageConsole
```

### Sample Output
```
================================================================================
JaCoCo Coverage Report
================================================================================
Class/Package                                      Instruction (%)      Branch (%)
--------------------------------------------------------------------------------
TOTAL                                                        85.50           78.25
com.example.service.UserService                             92.30           85.60
com.example.controller.UserController                       88.75           82.40
--------------------------------------------------------------------------------
```

## 🔧 Configuration

### Extension Configuration

**Kotlin DSL (build.gradle.kts):**
```kotlin
jacocoCoverageConsole {
    csvReportPath.set(file("build/reports/jacoco/test/jacocoTestReport.csv"))
    showTotal.set(true)
    targetClasses.set(listOf("com.example.service.*", "com.example.controller.*"))
}
```

**Groovy DSL (build.gradle):**
```groovy
jacocoCoverageConsole {
    csvReportPath = file('build/reports/jacoco/test/jacocoTestReport.csv')
    showTotal = true
    targetClasses = ['com.example.service.*', 'com.example.controller.*']
}
```

### Command Line Options
```bash
# Specific classes
./gradlew jacocoCoverageConsole --classes=com.example.service.*

# Custom CSV file
./gradlew jacocoCoverageConsole --csv-path=custom/path/report.csv

# Multiple targets  
./gradlew jacocoCoverageConsole --classes=com.example.service.*,com.example.controller.UserController
```

### JaCoCo CSV Report Setup
```kotlin
tasks.jacocoTestReport {
    reports {
        csv.required.set(true)
    }
}
```

## 🛠️ Development

### Quick Commands
```bash
./gradlew codefix        # Format and fix code
./gradlew qualityCheck   # Run all quality checks
./gradlew build          # Full build with tests
```

### Requirements
- **Gradle**: 4.x - 8.x  
- **Java**: 8+
- **Kotlin**: 1.3+

## 📚 Documentation

- **[Contributing Guide](CONTRIBUTING.md)** - Development setup and contribution guidelines
- **[Code Quality](CODE_QUALITY.md)** - Quality standards and tools
- **[Publishing Guide](PUBLISHING.md)** - For maintainers

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details on:
- Development environment setup
- Code quality standards  
- Testing requirements
- Pull request process

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📋 Changelog

See [CHANGELOG.md](CHANGELOG.md) for release history and changes.