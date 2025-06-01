# Contributing to JaCoCo Coverage Console Plugin

Thank you for your interest in contributing! This guide will help you get started with contributing to this project.

## 🚀 Quick Start

### 1. Fork & Clone
```bash
git clone https://github.com/YOUR_USERNAME/jacoco-coverage-console-plugin.git
cd jacoco-coverage-console-plugin
```

### 2. Create Branch
```bash
git checkout -b feature/your-feature-name
```

### 3. Development Environment
- **Java**: 8+ (tested on 8, 11, 17, 21)
- **Gradle**: 7.6+ (wrapper included)
- **Kotlin**: 1.9+ (managed by Gradle)

## 🛠️ Development Workflow

### Essential Commands
```bash
# Format and fix code style
./gradlew codefix

# Run quality checks
./gradlew qualityCheck

# Build with all checks
./gradlew build

# Test in sample project
cd test-project && ./gradlew clean test jacocoTestReport jacocoCoverageConsole
```

## 📝 Code Standards

### Kotlin Style Guide
- Follow [Kotlin conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Max line length: 120 characters
- Use meaningful names
- Auto-format with: `./gradlew ktlintFormat`

### Code Quality
- **ktlint**: Formatting and style
- **detekt**: Static analysis
- **JaCoCo**: Test coverage (aim for >90%)

### Commit Messages
Follow [Conventional Commits](https://www.conventionalcommits.org/):
```
feat: add new coverage threshold option
fix: handle empty CSV files correctly
docs: update README with new examples
test: add unit tests for CSV parser
```

## 🧪 Testing

### Running Tests
```bash
# Unit tests with coverage
./gradlew test jacocoTestReport

# Integration tests
./gradlew integrationTest

# Full test suite
./gradlew check
```

### Writing Tests
- Place unit tests in `src/test/kotlin/`
- Use descriptive test names
- Test both happy path and edge cases
- Mock external dependencies

## 📋 Pull Request Process

### Before Submitting
1. ✅ All tests pass
2. ✅ Code quality checks pass
3. ✅ Documentation updated
4. ✅ CHANGELOG.md updated

### PR Guidelines
- Clear title and description
- Reference related issues
- Include test coverage for new features
- Update documentation as needed

### Review Process
1. Automated checks must pass
2. Code review by maintainers
3. Address feedback promptly
4. Squash commits before merge

## 🐛 Reporting Issues

### Bug Reports
Include:
- Clear problem description
- Steps to reproduce
- Expected vs actual behavior
- Environment details (Java version, Gradle version, OS)
- Sample project or code snippet

### Feature Requests
Include:
- Use case description
- Proposed solution
- Alternative considerations
- Willingness to implement

## 🎯 Areas for Contribution

### High Priority
- Performance improvements
- Additional output formats (HTML, XML)
- Integration with CI/CD systems
- Gradle configuration cache support

### Good First Issues
- Documentation improvements
- Test coverage expansion
- Code quality enhancements
- Example projects

## 🔧 Local Development Setup

### IDE Configuration
**IntelliJ IDEA/Android Studio:**
1. Import Gradle project
2. Enable Kotlin plugin
3. Configure code style (120 char limit)
4. Install ktlint plugin

### Debugging
```bash
# Run with debug info
./gradlew test --info --stacktrace

# Debug plugin in test project
./gradlew :test-project:jacocoCoverageConsole --debug-jvm
```

## 📚 Resources

### Documentation
- [Gradle Plugin Development Guide](https://docs.gradle.org/current/userguide/custom_plugins.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

### Project Structure
```
src/main/kotlin/          # Plugin source code
src/test/kotlin/          # Unit tests
test-project/            # Integration test project
docs/                    # Documentation
config/detekt/          # Code quality config
```

## 🤝 Community

### Getting Help
- 💬 [GitHub Discussions](https://github.com/mas0061/jacoco-coverage-console-plugin/discussions)
- 🐛 [Issue Tracker](https://github.com/mas0061/jacoco-coverage-console-plugin/issues)
- 📧 Direct maintainer contact for sensitive issues

### Recognition
Contributors are recognized in:
- CHANGELOG.md
- GitHub contributor list
- Release notes

Thank you for contributing to make this plugin better! 🎉