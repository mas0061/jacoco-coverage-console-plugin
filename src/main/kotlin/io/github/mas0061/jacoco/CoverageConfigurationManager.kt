package io.github.mas0061.jacoco

import org.gradle.api.Project
import java.io.File

/**
 * プラグインの設定とオプションを管理するクラス
 */
class CoverageConfigurationManager(
    private val project: Project,
    private val extension: JacocoCoverageExtension,
) {
    /**
     * XMLファイルのパスを決定する
     * 優先順位: 1. コマンドラインオプション 2. プロジェクトプロパティ 3. エクステンション設定 4. デフォルトパス
     */
    fun determineXmlFile(xmlPathOption: String): File {
        return when {
            // 1. コマンドラインオプション (Gradle 5+)
            xmlPathOption.isNotEmpty() -> project.file(xmlPathOption)
            // 2. プロジェクトプロパティ (Gradle 4との互換性のため)
            project.hasProperty("jacocoXmlPath") -> project.file(project.property("jacocoXmlPath").toString())
            // 3. エクステンション設定
            extension.xmlReportPath != null -> extension.xmlReportPath!!
            // 4. デフォルトパス
            else -> project.file("build/reports/jacoco/test/jacocoTestReport.xml")
        }
    }

    /**
     * CSVファイルのパスを決定する (非推奨)
     * 優先順位: 1. コマンドラインオプション 2. プロジェクトプロパティ 3. エクステンション設定 4. デフォルトパス
     */
    @Deprecated("Use determineXmlFile instead")
    fun determineCsvFile(csvPathOption: String): File {
        return when {
            // 1. コマンドラインオプション (Gradle 5+)
            csvPathOption.isNotEmpty() -> project.file(csvPathOption)
            // 2. プロジェクトプロパティ (Gradle 4との互換性のため)
            project.hasProperty("jacocoCsvPath") -> project.file(project.property("jacocoCsvPath").toString())
            // 3. エクステンション設定
            extension.csvReportPath != null -> extension.csvReportPath!!
            // 4. デフォルトパス
            else -> project.file("build/reports/jacoco/test/jacocoTestReport.csv")
        }
    }

    /**
     * ターゲットクラスのリストを決定する
     * 優先順位: 1. コマンドラインオプション 2. プロジェクトプロパティ 3. エクステンション設定
     */
    fun determineTargetClasses(classesOption: String): List<String> {
        // 1. コマンドラインオプション (Gradle 5+)
        val fromOption =
            if (classesOption.isNotEmpty()) {
                classesOption.split(",").map { it.trim() }
            } else {
                emptyList()
            }

        // 2. プロジェクトプロパティ (Gradle 4との互換性のため)
        val fromProperty =
            if (fromOption.isEmpty() && project.hasProperty("jacocoClasses")) {
                project.property("jacocoClasses").toString().split(",").map { it.trim() }
            } else {
                emptyList()
            }

        // 3. エクステンション設定
        val fromExtension = extension.targetClasses

        return fromOption.ifEmpty { fromProperty.ifEmpty { fromExtension } }
    }

    /**
     * 全体カバレッジの表示設定を取得する
     */
    fun shouldShowTotal(): Boolean {
        return extension.showTotal
    }
}
