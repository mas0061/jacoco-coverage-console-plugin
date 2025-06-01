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
     * CSVファイルのパスを決定する
     * 優先順位: 1. コマンドラインオプション 2. エクステンション設定 3. デフォルトパス
     */
    fun determineCsvFile(csvPathOption: String): File {
        return when {
            csvPathOption.isNotEmpty() -> File(csvPathOption)
            extension.csvReportPath.isPresent -> extension.csvReportPath.get()
            else -> project.file("build/reports/jacoco/test/jacocoTestReport.csv")
        }
    }

    /**
     * ターゲットクラスのリストを決定する
     * 優先順位: 1. コマンドラインオプション 2. エクステンション設定
     */
    fun determineTargetClasses(classesOption: String): List<String> {
        val fromOption =
            if (classesOption.isNotEmpty()) {
                classesOption.split(",").map { it.trim() }
            } else {
                emptyList()
            }

        val fromExtension = extension.targetClasses.getOrElse(emptyList())

        return fromOption.ifEmpty { fromExtension }
    }

    /**
     * 全体カバレッジの表示設定を取得する
     */
    fun shouldShowTotal(): Boolean {
        return extension.showTotal.getOrElse(true)
    }
}
