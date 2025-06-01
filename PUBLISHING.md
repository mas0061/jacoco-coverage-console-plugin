# Publishing Guide

This document outlines the process for publishing new releases of the JaCoCo Coverage Console Plugin.

## 🚀 Quick Release Checklist

- [ ] Update version in `gradle.properties`
- [ ] Update `CHANGELOG.md`
- [ ] Run quality checks: `./gradlew qualityCheck`
- [ ] Create and push git tag
- [ ] Publish to Gradle Plugin Portal
- [ ] Create GitHub release

## 📋 Detailed Release Process

### 1. Pre-Release Preparation

#### Version Update
Update the version in `gradle.properties`:
```properties
version=1.1.0
```

#### Changelog Update
Add release notes to `CHANGELOG.md`:
```markdown
## [1.1.0] - 2024-XX-XX
### Added
- New feature description
### Fixed
- Bug fix description
```

#### Quality Assurance
```bash
# Run all quality checks
./gradlew qualityCheck

# Test in sample project
cd test-project && ./gradlew clean test jacocoTestReport jacocoCoverageConsole
```

### 2. Create Release Tag

```bash
# Create and push tag
git tag -a v1.1.0 -m "Release version 1.1.0"
git push origin v1.1.0
```

### 3. Publish to Gradle Plugin Portal

#### Setup API Keys
Add to `~/.gradle/gradle.properties`:
```properties
gradle.publish.key=YOUR_API_KEY
gradle.publish.secret=YOUR_API_SECRET
```

#### Publish Plugin
```bash
# Publish to Gradle Plugin Portal
./gradlew publishPlugins
```

### 4. Create GitHub Release

1. Go to [GitHub Releases](https://github.com/mas0061/jacoco-coverage-console-plugin/releases)
2. Click "Create a new release"
3. Select the tag (v1.1.0)
4. Copy changelog content as release notes
5. Attach JAR files from `build/libs/`
6. Publish release

## 🔧 Configuration

### Gradle Plugin Portal

The plugin is published with:
- **ID**: `io.github.mas0061.jacoco-coverage-console`
- **Display Name**: JaCoCo Coverage Console Plugin
- **Description**: Gradle plugin that displays JaCoCo test coverage in console

### Plugin Metadata

Located in `src/main/resources/META-INF/gradle-plugins/`:
```properties
implementation-class=io.github.mas0061.jacoco.JacocoCoverageConsolePlugin
```

## 📦 Artifacts

### Published Artifacts
- Main JAR: `jacoco-coverage-console-plugin-X.X.X.jar`
- Sources JAR: `jacoco-coverage-console-plugin-X.X.X-sources.jar`
- Javadoc JAR: `jacoco-coverage-console-plugin-X.X.X-javadoc.jar`

### Build Verification
```bash
# Verify build artifacts
./gradlew build
ls -la build/libs/
```

## 🔍 Verification Steps

### Pre-Publication Checks
1. ✅ Version number updated
2. ✅ Changelog updated
3. ✅ All tests pass
4. ✅ Code quality checks pass
5. ✅ Plugin works in test project

### Post-Publication Verification
1. Check [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.mas0061.jacoco-coverage-console)
2. Test installation in fresh project:
   ```kotlin
   plugins {
       id("io.github.mas0061.jacoco-coverage-console") version "X.X.X"
   }
   ```

## 🐛 Troubleshooting

### Common Issues

#### Publication Failed
```bash
# Check API credentials
gradle.publish.key and gradle.publish.secret in ~/.gradle/gradle.properties
```

#### Plugin Not Found
- Wait 10-15 minutes for indexing
- Check plugin ID spelling
- Verify version number

#### Build Failures
```bash
# Clean and rebuild
./gradlew clean build

# Check dependencies
./gradlew dependencies
```

## 📚 Resources

### Documentation
- [Gradle Plugin Portal Publishing](https://plugins.gradle.org/docs/publish-plugin)
- [Semantic Versioning](https://semver.org/)
- [GitHub Releases Guide](https://docs.github.com/en/repositories/releasing-projects-on-github)

### Automation Ideas
- GitHub Actions for automated publishing
- Automated changelog generation
- Version bump automation

## 🔐 Security Notes

### API Key Management
- Store keys securely in `~/.gradle/gradle.properties`
- Never commit API keys to repository
- Rotate keys periodically
- Use environment variables in CI/CD

### Release Verification
- Always test releases in isolated environment
- Verify plugin functionality before announcement
- Monitor for issues post-release

---

For questions about publishing, please open an issue or discussion on GitHub.