package io.github.mas0061.jacoco

import java.io.File

/**
 * JaCoCo Coverage Console プラグインの設定を管理するエクステンション
 *
 * このエクステンションを使用して、プラグインの動作をカスタマイズできます。
 *
 * 使用例:
 * ```kotlin
 * jacocoCoverageConsole {
 *     xmlReportPath = file("custom/path/jacoco.xml")
 *     showTotal = true
 *     showPackageSummary = true
 *     targetClasses = listOf("com.example.service.*", "com.example.model.User")
 * }
 * ```
 */
open class JacocoCoverageExtension {
    /**
     * JaCoCoのXMLレポートファイルのパス
     *
     * 指定しない場合は、デフォルトパス `build/reports/jacoco/test/jacocoTestReport.xml` が使用されます。
     */
    var xmlReportPath: File? = null

    /**
     * JaCoCoのCSVレポートファイルのパス (非推奨)
     *
     * 指定しない場合は、デフォルトパス `build/reports/jacoco/test/jacocoTestReport.csv` が使用されます。
     * @deprecated XMLレポートの使用を推奨します
     */
    @Deprecated("Use xmlReportPath instead for better functionality including project totals")
    var csvReportPath: File? = null

    /**
     * 全体のカバレッジ（Total行）を表示するかどうか
     *
     * デフォルト値: true
     *
     * - true: Total行を表示
     * - false: 個別のクラス/パッケージのみ表示
     */
    var showTotal: Boolean = true

    /**
     * パッケージサマリーを表示するかどうか
     *
     * デフォルト値: true
     *
     * - true: パッケージレベルのカバレッジサマリーを表示
     * - false: クラスレベルの詳細のみ表示
     */
    var showPackageSummary: Boolean = true

    /**
     * 特定のクラス/パッケージのカバレッジを表示する際のターゲット
     *
     * 空のリストの場合、すべてのクラス/パッケージが対象となります。
     *
     * サポートされるパターン:
     * - 完全一致: "com.example.service.UserService"
     * - パッケージワイルドカード: "com.example.service.*"
     * - パッケージ名: "com.example.service"
     *
     * 例:
     * ```kotlin
     * targetClasses = listOf(
     *     "com.example.service.*",
     *     "com.example.model.User"
     * )
     * ```
     */
    var targetClasses: List<String> = emptyList()

    /**
     * レポートファイルの設定を検証する
     */
    internal fun validateConfiguration() {
        // XMLレポートファイルを優先してチェック
        val reportFile = xmlReportPath ?: csvReportPath
        reportFile?.let { file ->
            require(file.exists()) {
                "Specified report file does not exist: ${file.absolutePath}"
            }
            require(file.canRead()) {
                "Specified report file is not readable: ${file.absolutePath}"
            }
        }

        // ターゲットクラスの設定を検証
        targetClasses.forEach { target ->
            require(target.isNotBlank()) {
                "Target class/package name cannot be blank"
            }
        }
    }

    /**
     * 使用するレポート形式がXMLかどうかを判定
     * CSVが明示的に設定されている場合のみCSVを使用し、それ以外はXMLを使用
     */
    internal fun isUsingXmlReport(): Boolean {
        return csvReportPath == null
    }
}
