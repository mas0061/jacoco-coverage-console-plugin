# GitHub Copilot Instructions

This is a Gradle plugin project for displaying JaCoCo coverage reports in console table format.

## Project Overview

- **Type**: Gradle Plugin
- **Language**: Kotlin (JVM target 1.8)
- **Purpose**: Parse JaCoCo CSV reports and display coverage information in console
- **Compatibility**: Gradle 4.x - 8.x, Java 8+

## Code Style Guidelines

- Follow Kotlin official style guide (enforced by ktlint)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions focused and small
- Use property delegation where appropriate

## Architecture Patterns

- **Plugin Class**: `JacocoCoverageConsolePlugin` - Main plugin entry point
- **Extension Class**: `JacocoCoverageConsoleExtension` - Configuration DSL
- **Task Class**: `JacocoCoverageConsoleTask` - Main functionality
- **Data Classes**: Use for CSV parsing and report data structures
- **Utility Functions**: Keep in separate utility classes

## Testing Approach

- Use JUnit 4 for unit tests
- Use Gradle TestKit for functional testing
- Mock external dependencies with Mockito
- Test both positive and negative scenarios
- Include edge cases (empty files, malformed CSV, etc.)

## Dependencies to Prefer

- Prefer Gradle API over external libraries when possible
- Use standard Java/Kotlin libraries for file operations
- Avoid heavy dependencies to keep plugin lightweight
- Stick to Gradle-compatible versions

## Common Patterns

### Plugin Configuration
```kotlin
// Extension configuration
jacocoCoverageConsole {
    csvReportPath.set(file("path/to/report.csv"))
    showTotal.set(true)
    targetClasses.set(listOf("com.example.*"))
}
```

### Task Implementation
```kotlin
@TaskAction
fun execute() {
    // Validate inputs
    // Process CSV file
    // Generate console output
}
```

### CSV Parsing
- Handle malformed CSV gracefully
- Validate required columns exist
- Support different JaCoCo CSV formats

## File Naming Conventions

- Plugin class: `*Plugin.kt`
- Extension class: `*Extension.kt` 
- Task class: `*Task.kt`
- Test class: `*Test.kt`
- Utility class: `*Utils.kt` or `*Helper.kt`

## Documentation Style

- Use clear, concise English
- Include code examples in README
- Document configuration options thoroughly
- Provide troubleshooting guidance

## Quality Standards

- All code must pass ktlint formatting
- All code must pass detekt static analysis
- Maintain test coverage above 80%
- No build warnings allowed

## Gradle Best Practices

- Use Provider API for lazy evaluation
- Make tasks cacheable when possible
- Follow Gradle conventions for input/output
- Use appropriate task dependencies