# 🔍 Code Quality Guidelines

This project uses the following tools to maintain code quality.

## 🛠️ Tool Configuration

### ktlint
- **Purpose**: Kotlin code style checking and formatting
- **Configuration**: Follows official Kotlin style guide
- **Execution**: Auto-formatting and manual checking

### detekt
- **Purpose**: Static analysis for code quality checks
- **Configuration**: Customized via `config/detekt/detekt.yml`
- **Checks**: Complexity, naming conventions, potential bugs, performance issues

## 🚀 Development Workflow

### Daily Development
```bash
# Auto-format before/after writing code
./gradlew codefix

# Quality check before commit
./gradlew qualityCheck

# Regular test execution (includes ktlint check)
./gradlew test
```

### Individual Execution
```bash
# Format only
./gradlew ktlintFormat

# Style check only
./gradlew ktlintCheck

# Static analysis only
./gradlew detekt

# Full build (includes quality checks)
./gradlew build
```

## 🔄 CI/CD Integration

### Automated Checks
- **On Pull Requests**: Basic quality checks (CI)
- **On PR Creation**: Detailed quality analysis and automated comments

### Workflow
Integrated CI workflow executes:

1. **Test Job** 
   - Multi-version testing (Java 8,11,17,21 × Gradle 8.0,8.14)
   - ktlint + detekt + test execution
   - Report storage for each version combination

2. **Code Quality Job**
   - Detailed quality analysis (Java 17 + latest Gradle)
   - JaCoCo coverage report generation
   - Automated quality report comments on PRs
   - Long-term quality report storage

## 📋 Quality Standards

### Required Conditions (Automatically checked in CI)
- ✅ Pass ktlint style checks
- ✅ Pass detekt static analysis
- ✅ All tests pass
- ✅ Build succeeds

### Recommendations
- Add appropriate tests for new features
- Write comments in English
- Add explanatory comments for complex logic

## 🚨 Troubleshooting

### ktlint Errors
```bash
# Try auto-fix
./gradlew ktlintFormat

# Manual check and fix
./gradlew ktlintCheck
```

### detekt Errors
```bash
# Check report
open build/reports/detekt/detekt.html

# Adjust configuration if needed
vi config/detekt/detekt.yml
```

### CI Errors
1. Run `./gradlew qualityCheck` locally
2. Fix issues and commit
3. Push again

## 📊 Report Review

### After Local Execution
- **ktlint**: `build/reports/ktlint/`
- **detekt**: `build/reports/detekt/detekt.html`
- **Tests**: `build/reports/tests/test/index.html`

### GitHub Actions
- Download artifacts from Actions screen
- Check quality status in PR comments

## 💡 Best Practices

### Before Commit
```bash
./gradlew codefix qualityCheck test
```

### Before Creating PR
```bash
./gradlew build
```

### When Developing New Features
1. Develop with test-first approach
2. Run quality checks regularly
3. Check PR template checklist