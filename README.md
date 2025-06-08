# JaCoCo Coverage Console Plugin

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.mas0061.jacoco-coverage-console?logo=gradle)](https://plugins.gradle.org/plugin/io.github.mas0061.jacoco-coverage-console)
[![CI](https://github.com/mas0061/jacoco-coverage-console-plugin/workflows/CI/badge.svg)](https://github.com/mas0061/jacoco-coverage-console-plugin/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A Gradle plugin that outputs AI-readable JaCoCo coverage reports to the console with project totals and package summaries, optimized for AI-assisted development workflows.

## 🎯 Why This Plugin?

This plugin was created for AI-assisted development workflows, providing coverage information in a clean, structured format that both developers and AI tools can easily read and understand.

**What makes it unique:**
- **Project Totals**: See overall project coverage at a glance
- **Package Summaries**: Hierarchical view with package-level aggregation
- **Granular Filtering**: Focus on specific classes or packages instead of just total coverage
- **AI-Optimized Output**: Structured tabular format that AI assistants can parse alongside your code
- **Wildcard Support**: Use patterns like `com.example.service.*` to track multiple related classes
- **Real-time Feedback**: Coverage data stays in the console for immediate access during development

## ✨ Features

- 📊 Parse JaCoCo XML reports with project totals and package summaries
- 🎯 Filter by specific classes, packages, or use wildcards
- 🤖 Clean console output optimized for both humans and AI
- ⚙️ Configure via build.gradle or command-line options
- 🔧 Compatible with Gradle 4-8 and Java 8+
- 📈 Display hierarchical coverage: Project → Package → Class

## 📋 Compatibility

| Gradle Version | Build Script | Status |
|----------------|--------------|--------|
| 4.x | Groovy DSL | ✅ Supported |
| 4.x | Kotlin DSL | ❌ Not supported* |
| 5.x - 8.x | Kotlin DSL | ✅ Supported |
| 5.x - 8.x | Groovy DSL | ✅ Supported |

*For Gradle 4 users: Use Groovy DSL instead of Kotlin DSL due to Kotlin version compatibility issues.

## 🚀 Quick Start

### Installation

**Kotlin DSL (build.gradle.kts):**
```kotlin
plugins {
    id("io.github.mas0061.jacoco-coverage-console") version "1.0.0"
}
```

**Groovy DSL (build.gradle):**
```groovy
plugins {
    id 'io.github.mas0061.jacoco-coverage-console' version '1.0.0'
}
```

### Basic Usage
```bash
# 1. Ensure JaCoCo XML reports are enabled (default)
# 2. Run tests with coverage
./gradlew test jacocoTestReport jacocoCoverageConsole
```

### Sample Output
```
==================================================================================
JaCoCo Coverage Report
==================================================================================
Class/Package                                      Instruction (%)      Branch (%)
----------------------------------------------------------------------------------
PROJECT TOTAL                                                85.50           78.25
com.example.service (package)                               90.20           82.10
com.example.controller (package)                            88.75           82.40
  com.example.service.UserService                           92.30           85.60
  com.example.service.UserRepository                        88.10           78.60
  com.example.controller.UserController                     88.75           82.40
----------------------------------------------------------------------------------
```

## 💡 Use Cases

### Focused Development
```bash
# Working on a specific class
./gradlew jacocoCoverageConsole --classes=com.example.service.UserService

# Refactoring an entire package
./gradlew jacocoCoverageConsole --classes=com.example.service.*

# Testing multiple components
./gradlew jacocoCoverageConsole --classes=com.example.service.*,com.example.controller.*
```

### AI-Assisted Workflow
1. Write code with AI assistance
2. Run coverage for the specific class/package
3. AI reads the hierarchical output (project → package → class) and suggests which tests to add
4. Monitor package-level progress alongside individual class coverage
5. Repeat until desired coverage is achieved

## 🔧 Configuration

### Extension Configuration

**Kotlin DSL (build.gradle.kts):**
```kotlin
jacocoCoverageConsole {
    xmlReportPath = file("build/reports/jacoco/test/jacocoTestReport.xml")
    showTotal = true                    // Show PROJECT TOTAL row (default: true)
    showPackageSummary = true          // Show package-level summaries (default: true)
    targetClasses = listOf("com.example.service.*", "com.example.controller.*")
}
```

**Groovy DSL (build.gradle):**
```groovy
jacocoCoverageConsole {
    xmlReportPath = file('build/reports/jacoco/test/jacocoTestReport.xml')
    showTotal = true                    // Show PROJECT TOTAL row (default: true)
    showPackageSummary = true          // Show package-level summaries (default: true)
    targetClasses = ['com.example.service.*', 'com.example.controller.*']
}
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `xmlReportPath` | File | Auto-detected | Path to JaCoCo XML report file |
| `csvReportPath` | File | Auto-detected | Path to JaCoCo CSV report file (deprecated) |
| `showTotal` | Boolean | `true` | Display PROJECT TOTAL coverage |
| `showPackageSummary` | Boolean | `true` | Display package-level summaries |
| `targetClasses` | List<String> | `[]` | Filter specific classes/packages |

### Command Line Options

#### Gradle 5+ (Using --options)
```bash
# Specific classes
./gradlew jacocoCoverageConsole --classes=com.example.service.*

# Custom XML file
./gradlew jacocoCoverageConsole --xml-path=custom/path/report.xml

# Multiple targets  
./gradlew jacocoCoverageConsole --classes=com.example.service.*,com.example.controller.UserController
```

#### Gradle 4 (Using -P project properties)
```bash
# Specific classes
./gradlew jacocoCoverageConsole -PjacocoClasses=com.example.service.*

# Custom XML file
./gradlew jacocoCoverageConsole -PjacocoXmlPath=custom/path/report.xml

# Multiple targets  
./gradlew jacocoCoverageConsole -PjacocoClasses=com.example.service.*,com.example.controller.UserController
```

> **Note**: Gradle 4 does not support the `@Option` annotation, so project properties must be used instead.

### JaCoCo XML Report Setup

**XML reports are enabled by default in JaCoCo**, but you can explicitly configure them:

**Kotlin DSL (Gradle 5+):**
```kotlin
tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false) // Optional: disable CSV if not needed
    }
}
```

**Groovy DSL (All Gradle versions):**
```groovy
jacocoTestReport {
    reports {
        xml.enabled = true
        csv.enabled = false // Optional: disable CSV if not needed
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

## 📚 Documentation

- **[Contributing Guide](CONTRIBUTING.md)** - Development setup and contribution guidelines
- **[Code Quality](CODE_QUALITY.md)** - Quality standards and tools

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details on:
- Development environment setup
- Code quality standards  
- Testing requirements
- Pull request process

## 🤖 About This Project

This plugin was created almost entirely with AI assistance, demonstrating the power of AI-assisted development. It's a meta example - a tool designed for AI-assisted coding that was itself built through AI collaboration.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📋 Changelog

See [CHANGELOG.md](CHANGELOG.md) for release history and changes.