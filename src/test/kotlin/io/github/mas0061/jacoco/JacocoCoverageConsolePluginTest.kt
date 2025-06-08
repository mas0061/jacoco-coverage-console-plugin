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

    companion object {
        private const val TEST_XML_CONTENT = """<?xml version="1.0" encoding="UTF-8"?>
<report name="test-project">
    <counter type="INSTRUCTION" missed="30" covered="170"/>
    <counter type="BRANCH" missed="13" covered="27"/>
    <counter type="LINE" missed="9" covered="31"/>
    <counter type="COMPLEXITY" missed="6" covered="14"/>
    <counter type="METHOD" missed="3" covered="7"/>
    <counter type="CLASS" missed="0" covered="2"/>
    
    <package name="com.example">
        <counter type="INSTRUCTION" missed="10" covered="90"/>
        <counter type="BRANCH" missed="5" covered="15"/>
        <counter type="LINE" missed="3" covered="17"/>
        <counter type="COMPLEXITY" missed="2" covered="8"/>
        <counter type="METHOD" missed="1" covered="4"/>
        <counter type="CLASS" missed="0" covered="1"/>
        
        <class name="com/example/TestClass" sourcefilename="TestClass.java">
            <counter type="INSTRUCTION" missed="10" covered="90"/>
            <counter type="BRANCH" missed="5" covered="15"/>
            <counter type="LINE" missed="3" covered="17"/>
            <counter type="COMPLEXITY" missed="2" covered="8"/>
            <counter type="METHOD" missed="1" covered="4"/>
            <counter type="CLASS" missed="0" covered="1"/>
        </class>
    </package>
    
    <package name="com.example.service">
        <counter type="INSTRUCTION" missed="20" covered="80"/>
        <counter type="BRANCH" missed="8" covered="12"/>
        <counter type="LINE" missed="6" covered="14"/>
        <counter type="COMPLEXITY" missed="4" covered="6"/>
        <counter type="METHOD" missed="2" covered="3"/>
        <counter type="CLASS" missed="0" covered="1"/>
        
        <class name="com/example/service/UserService" sourcefilename="UserService.java">
            <counter type="INSTRUCTION" missed="20" covered="80"/>
            <counter type="BRANCH" missed="8" covered="12"/>
            <counter type="LINE" missed="6" covered="14"/>
            <counter type="COMPLEXITY" missed="4" covered="6"/>
            <counter type="METHOD" missed="2" covered="3"/>
            <counter type="CLASS" missed="0" covered="1"/>
        </class>
    </package>
</report>"""
    }

    @Before
    fun setUp() {
        setupProjectFiles()
        setupReportFiles()
    }

    private fun setupProjectFiles() {
        buildFile = tempFolder.newFile("build.gradle")
        settingsFile = tempFolder.newFile("settings.gradle")
        settingsFile.writeText("rootProject.name = 'test-project'")
    }

    private fun setupReportFiles() {
        val reportsDir = tempFolder.newFolder("build", "reports", "jacoco", "test")
        csvReportFile = File(reportsDir, "jacocoTestReport.csv")

        createXmlReportFile(reportsDir)
        createCsvReportFile()
    }

    private fun createXmlReportFile(reportsDir: File) {
        val xmlReportFile = File(reportsDir, "jacocoTestReport.xml")
        xmlReportFile.writeText(TEST_XML_CONTENT)
    }

    private fun createCsvReportFile() {
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
