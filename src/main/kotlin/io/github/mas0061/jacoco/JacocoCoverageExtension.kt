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
 *     csvReportPath = file("custom/path/jacoco.csv")
 *     showTotal = false
 *     targetClasses = listOf("com.example.service.*", "com.example.model.User")
 * }
 * ```
 */
open class JacocoCoverageExtension {
    /**
     * JaCoCoのCSVレポートファイルのパス
     *
     * 指定しない場合は、デフォルトパス `build/reports/jacoco/test/jacocoTestReport.csv` が使用されます。
     */
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
     * CSVレポートファイルの設定を検証する
     */
    internal fun validateConfiguration() {
        csvReportPath?.let { file ->
            require(file.exists()) {
                "Specified CSV report file does not exist: ${file.absolutePath}"
            }
            require(file.canRead()) {
                "Specified CSV report file is not readable: ${file.absolutePath}"
            }
        }

        // ターゲットクラスの設定を検証
        targetClasses.forEach { target ->
            require(target.isNotBlank()) {
                "Target class/package name cannot be blank"
            }
        }
    }
}
