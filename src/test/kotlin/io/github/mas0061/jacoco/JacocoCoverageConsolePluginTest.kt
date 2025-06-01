package io.github.mas0061.jacoco

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class JacocoCoverageConsolePluginTest {
    @Rule
    @JvmField
    val tempFolder = TemporaryFolder()

    private lateinit var buildFile: File
    private lateinit var settingsFile: File
    private lateinit var csvReportFile: File

    @Before
    fun setUp() {
        buildFile = tempFolder.newFile("build.gradle")
        settingsFile = tempFolder.newFile("settings.gradle")

        // settings.gradle を作成
        settingsFile.writeText("rootProject.name = 'test-project'")

        // JaCoCoレポート用ディレクトリを作成
        val reportsDir = tempFolder.newFolder("build", "reports", "jacoco", "test")
        csvReportFile = File(reportsDir, "jacocoTestReport.csv")

        // テスト用CSVファイルを作成
        csvReportFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            test-project,com.example,TestClass,10,90,5,15,3,17,2,8,1,4
            test-project,com.example.service,UserService,20,80,8,12,6,14,4,6,2,3
            test-project,,Total,30,170,13,27,9,31,6,14,3,7
            """.trimIndent(),
        )
    }

    @Test
    fun testPluginCanBeApplied() {
        buildFile.writeText(
            """
            plugins {
                id 'io.github.mas0061.jacoco-coverage-console'
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner.create()
                .withProjectDir(tempFolder.root)
                .withArguments("tasks", "--group=reporting")
                .withPluginClasspath()
                .build()

        assertTrue(result.output.contains("jacocoCoverageConsole"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":tasks")?.outcome)
    }

    @Test
    fun testJacocoCoverageTaskWithDefaultSettings() {
        buildFile.writeText(
            """
            plugins {
                id 'io.github.mas0061.jacoco-coverage-console'
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner.create()
                .withProjectDir(tempFolder.root)
                .withArguments("jacocoCoverageConsole")
                .withPluginClasspath()
                .build()

        assertTrue(result.output.contains("JaCoCo Coverage Report"))
        assertTrue(result.output.contains("TOTAL"))
        assertTrue(result.output.contains("85.00")) // instruction coverage
        assertTrue(result.output.contains("67.50")) // branch coverage
        assertEquals(TaskOutcome.SUCCESS, result.task(":jacocoCoverageConsole")?.outcome)
    }

    @Test
    fun testJacocoCoverageTaskWithSpecificClasses() {
        buildFile.writeText(
            """
            plugins {
                id 'io.github.mas0061.jacoco-coverage-console'
            }
            
            jacocoCoverageConsole {
                showTotal = false
                targetClasses = ['com.example.TestClass']
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner.create()
                .withProjectDir(tempFolder.root)
                .withArguments("jacocoCoverageConsole")
                .withPluginClasspath()
                .build()

        assertTrue(result.output.contains("JaCoCo Coverage Report"))
        assertTrue(result.output.contains("com.example.TestClass"))
        assertFalse(result.output.contains("TOTAL"))
        assertTrue(result.output.contains("90.00")) // TestClass instruction coverage
        assertEquals(TaskOutcome.SUCCESS, result.task(":jacocoCoverageConsole")?.outcome)
    }

    @Test
    fun testJacocoCoverageTaskWithCommandLineOptions() {
        buildFile.writeText(
            """
            plugins {
                id 'io.github.mas0061.jacoco-coverage-console'
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner.create()
                .withProjectDir(tempFolder.root)
                .withArguments("jacocoCoverageConsole", "--classes=com.example.service.UserService")
                .withPluginClasspath()
                .build()

        assertTrue(result.output.contains("JaCoCo Coverage Report"))
        assertTrue(result.output.contains("com.example.service.UserService"))
        assertTrue(result.output.contains("80.00")) // UserService instruction coverage
        assertEquals(TaskOutcome.SUCCESS, result.task(":jacocoCoverageConsole")?.outcome)
    }

    @Test
    fun testJacocoCoverageTaskWithCustomCsvPath() {
        // カスタムパスにCSVファイルを作成
        val customDir = tempFolder.newFolder("custom", "path")
        val customCsvFile = File(customDir, "custom-report.csv")
        customCsvFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            test-project,com.custom,CustomClass,5,95,2,18,1,19,1,9,0,5
            """.trimIndent(),
        )

        buildFile.writeText(
            """
            plugins {
                id 'io.github.mas0061.jacoco-coverage-console'
            }
            
            jacocoCoverageConsole {
                csvReportPath = file('custom/path/custom-report.csv')
                showTotal = false
                targetClasses = ['com.custom.CustomClass']
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner.create()
                .withProjectDir(tempFolder.root)
                .withArguments("jacocoCoverageConsole")
                .withPluginClasspath()
                .build()

        assertTrue(result.output.contains("com.custom.CustomClass"))
        assertTrue(result.output.contains("95.00")) // CustomClass instruction coverage
        assertEquals(TaskOutcome.SUCCESS, result.task(":jacocoCoverageConsole")?.outcome)
    }

    @Test
    fun testJacocoCoverageTaskWithNonExistentFile() {
        buildFile.writeText(
            """
            plugins {
                id 'io.github.mas0061.jacoco-coverage-console'
            }
            
            jacocoCoverageConsole {
                csvReportPath = file('non-existent.csv')
            }
            """.trimIndent(),
        )

        val result =
            GradleRunner.create()
                .withProjectDir(tempFolder.root)
                .withArguments("jacocoCoverageConsole")
                .withPluginClasspath()
                .buildAndFail()

        assertTrue(result.output.contains("CSV file not found"))
        assertEquals(TaskOutcome.FAILED, result.task(":jacocoCoverageConsole")?.outcome)
    }
}
