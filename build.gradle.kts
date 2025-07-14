plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    jacoco
    id("com.gradle.plugin-publish") version "1.3.1"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

group = "io.github.mas0061"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.18.0")
    testImplementation(gradleTestKit())
}

gradlePlugin {
    website = "https://github.com/mas0061/jacoco-coverage-console-plugin"
    vcsUrl = "https://github.com/mas0061/jacoco-coverage-console-plugin.git"

    plugins {
        create("jacocoCoverageConsole") {
            id = "io.github.mas0061.jacoco-coverage-console"
            implementationClass = "io.github.mas0061.jacoco.JacocoCoverageConsolePlugin"
            displayName = "JaCoCo Coverage Console Plugin"
            description = "Displays JaCoCo coverage reports in console table format"
            tags = listOf("jacoco", "coverage", "testing", "reporting")
        }
    }
}

repositories {
    mavenCentral()
}

// JaCoCo設定
jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(true)
    }
}

// ktlint設定
ktlint {
    version.set("1.0.1")
    debug.set(false)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
}

// detekt設定
detekt {
    toolVersion = "1.23.4"
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}

// カスタムタスク: コード品質チェック統合
tasks.register("qualityCheck") {
    description = "Run all code quality checks (ktlint, detekt)"
    group = "verification"

    dependsOn("ktlintCheck", "detekt")
}

// カスタムタスク: コード修正とフォーマット
tasks.register("codefix") {
    description = "Auto-fix code style and format issues"
    group = "formatting"

    dependsOn("ktlintFormat")
}

// buildタスクでコード品質チェックを自動実行
tasks.named("build") {
    dependsOn("qualityCheck")
}

// testタスクの前にコード品質チェックを実行
tasks.named("test") {
    dependsOn("ktlintCheck")
}

// Plugin Portal公開用の追加設定
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("JaCoCo Coverage Console Plugin")
                description.set(
                    "A Gradle plugin that parses JaCoCo CSV reports and displays coverage information in a console table format",
                )
                url.set("https://github.com/mas0061/jacoco-coverage-console-plugin")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("mas0061")
                        name.set("mas0061")
                        url.set("https://github.com/mas0061")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/mas0061/jacoco-coverage-console-plugin.git")
                    developerConnection.set("scm:git:ssh://github.com/mas0061/jacoco-coverage-console-plugin.git")
                    url.set("https://github.com/mas0061/jacoco-coverage-console-plugin")
                }
            }
        }
    }
}
