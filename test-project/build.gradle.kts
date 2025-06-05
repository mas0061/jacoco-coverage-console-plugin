plugins {
    id("java")
    id("jacoco")
    id("io.github.mas0061.jacoco-coverage-console") version "0.0.3"
}

group = "com.example"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.11.0")
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
        csv.required.set(true) // CSVレポートを有効化
    }
    
    finalizedBy("jacocoCoverageConsole") // テストレポート生成後に自動実行
}

tasks.test {
    useJUnit()
    finalizedBy(tasks.jacocoTestReport)
}

// プラグイン設定
jacocoCoverageConsole {
    // デフォルトパスを使用（build/reports/jacoco/test/jacocoTestReport.csv）
    showTotal = true
    // 特定のパッケージのみ表示
    targetClasses = listOf("com.example.service.*", "com.example.model.*")
}