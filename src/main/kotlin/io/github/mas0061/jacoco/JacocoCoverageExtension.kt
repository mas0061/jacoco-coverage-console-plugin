package io.github.mas0061.jacoco

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import java.io.File
import javax.inject.Inject

/**
 * JaCoCo Coverage Console プラグインの設定を管理するエクステンション
 *
 * このエクステンションを使用して、プラグインの動作をカスタマイズできます。
 *
 * 使用例:
 * ```kotlin
 * jacocoCoverageConsole {
 *     csvReportPath.set(file("custom/path/jacoco.csv"))
 *     showTotal.set(false)
 *     targetClasses.set(listOf("com.example.service.*", "com.example.model.User"))
 * }
 * ```
 */
abstract class JacocoCoverageExtension
    @Inject
    constructor(private val objects: ObjectFactory) {
        /**
         * JaCoCoのCSVレポートファイルのパス
         *
         * 指定しない場合は、デフォルトパス `build/reports/jacoco/test/jacocoTestReport.csv` が使用されます。
         */
        abstract val csvReportPath: Property<File>

        /**
         * 全体のカバレッジ（Total行）を表示するかどうか
         *
         * デフォルト値: true
         *
         * - true: Total行を表示
         * - false: 個別のクラス/パッケージのみ表示
         */
        abstract val showTotal: Property<Boolean>

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
         * targetClasses.set(listOf(
         *     "com.example.service.*",
         *     "com.example.model.User"
         * ))
         * ```
         */
        val targetClasses: ListProperty<String> = objects.listProperty(String::class.java)

        init {
            // デフォルト値を設定
            showTotal.convention(true)
            targetClasses.convention(emptyList())
        }

        /**
         * CSVレポートファイルの設定を検証する
         */
        internal fun validateConfiguration() {
            if (csvReportPath.isPresent) {
                val file = csvReportPath.get()
                require(file.exists()) {
                    "Specified CSV report file does not exist: ${file.absolutePath}"
                }
                require(file.canRead()) {
                    "Specified CSV report file is not readable: ${file.absolutePath}"
                }
            }

            // ターゲットクラスの設定を検証
            val targets = targetClasses.getOrElse(emptyList())
            targets.forEach { target ->
                require(target.isNotBlank()) {
                    "Target class/package name cannot be blank"
                }
            }
        }
    }
