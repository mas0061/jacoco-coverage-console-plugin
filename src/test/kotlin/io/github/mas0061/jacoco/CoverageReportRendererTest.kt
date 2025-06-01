package io.github.mas0061.jacoco

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CoverageReportRendererTest {
    private val renderer = CoverageReportRenderer()
    private val originalOut = System.out
    private val outputStream = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @After
    fun tearDown() {
        System.setOut(originalOut)
    }

    @Test
    fun testRenderCoverageTable() {
        val testData =
            listOf(
                CoverageRow(
                    group = "test-project",
                    packageName = "com.example.service",
                    className = "UserService",
                    instructionMissed = 10,
                    instructionCovered = 90,
                    branchMissed = 5,
                    branchCovered = 15,
                    lineMissed = 2,
                    lineCovered = 18,
                    complexityMissed = 1,
                    complexityCovered = 9,
                    methodMissed = 0,
                    methodCovered = 5,
                ),
                CoverageRow(
                    group = "test-project",
                    packageName = "",
                    className = "Total",
                    instructionMissed = 10,
                    instructionCovered = 90,
                    branchMissed = 5,
                    branchCovered = 15,
                    lineMissed = 2,
                    lineCovered = 18,
                    complexityMissed = 1,
                    complexityCovered = 9,
                    methodMissed = 0,
                    methodCovered = 5,
                ),
            )

        renderer.renderCoverageTable(testData)

        val output = outputStream.toString()

        // ヘッダーの確認
        assert(output.contains("JaCoCo Coverage Report"))
        assert(output.contains("Class/Package"))
        assert(output.contains("Instruction (%)"))
        assert(output.contains("Branch (%)"))

        // データ行の確認
        assert(output.contains("com.example.service.UserService"))
        assert(output.contains("TOTAL"))
        assert(output.contains("90.00"))
        assert(output.contains("75.00"))
    }

    @Test
    fun testRenderEmptyData() {
        renderer.renderCoverageTable(emptyList())

        val output = outputStream.toString()

        // ヘッダーは表示されるが、データ行は表示されない
        assert(output.contains("JaCoCo Coverage Report"))
        assert(output.contains("Class/Package"))
    }
}
