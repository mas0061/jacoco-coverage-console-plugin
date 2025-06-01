package io.github.mas0061.jacoco

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CsvReportParserTest {
    @Rule
    @JvmField
    val tempFolder = TemporaryFolder()

    private lateinit var parser: CsvReportParser
    private lateinit var csvFile: File

    @Before
    fun setUp() {
        parser = CsvReportParser()
    }

    @Test
    fun testParseValidCsvFile() {
        // テスト用CSVファイルを作成
        csvFile = tempFolder.newFile("test-report.csv")
        csvFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            test-project,com.example,TestClass,10,90,5,15,3,17,2,8,1,4
            test-project,com.example.service,UserService,20,80,8,12,6,14,4,6,2,3
            test-project,,Total,30,170,13,27,9,31,6,14,3,7
            """.trimIndent(),
        )

        val result = parser.parse(csvFile)

        assertEquals(3, result.size)

        // 最初の行をテスト
        val firstRow = result[0]
        assertEquals("test-project", firstRow.group)
        assertEquals("com.example", firstRow.packageName)
        assertEquals("TestClass", firstRow.className)
        assertEquals(10, firstRow.instructionMissed)
        assertEquals(90, firstRow.instructionCovered)
        assertEquals(5, firstRow.branchMissed)
        assertEquals(15, firstRow.branchCovered)
        assertEquals("com.example.TestClass", firstRow.fullClassName)

        // カバレッジ計算をテスト
        assertEquals(90.0, firstRow.instructionCoverage, 0.01)
        assertEquals(75.0, firstRow.branchCoverage, 0.01)

        // Total行をテスト
        val totalRow = result[2]
        assertEquals("Total", totalRow.className)
        assertEquals("", totalRow.packageName)
        assertEquals("Total", totalRow.fullClassName)
    }

    @Test
    fun testParseCsvWithEmptyPackage() {
        csvFile = tempFolder.newFile("test-empty-package.csv")
        csvFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            test-project,,RootClass,5,45,2,8,2,18,1,9,0,5
            """.trimIndent(),
        )

        val result = parser.parse(csvFile)

        assertEquals(1, result.size)
        val row = result[0]
        assertEquals("", row.packageName)
        assertEquals("RootClass", row.className)
        assertEquals("RootClass", row.fullClassName)
    }

    @Test
    fun testCoverageCalculationWithZeroTotal() {
        csvFile = tempFolder.newFile("test-zero-coverage.csv")
        csvFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            test-project,com.example,EmptyClass,0,0,0,0,0,0,0,0,0,0
            """.trimIndent(),
        )

        val result = parser.parse(csvFile)

        assertEquals(1, result.size)
        val row = result[0]
        assertEquals(0.0, row.instructionCoverage, 0.01)
        assertEquals(0.0, row.branchCoverage, 0.01)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseNonExistentFile() {
        val nonExistentFile = File("non-existent-file.csv")
        parser.parse(nonExistentFile)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseInvalidCsvFormat() {
        csvFile = tempFolder.newFile("invalid-format.csv")
        csvFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED
            test-project,com.example,TestClass,10
            """.trimIndent(),
        )

        parser.parse(csvFile)
    }

    @Test
    fun testParseEmptyFile() {
        csvFile = tempFolder.newFile("empty.csv")
        csvFile.writeText(
            "GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED," +
                "LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED",
        )

        val result = parser.parse(csvFile)

        assertTrue(result.isEmpty())
    }

    @Test
    fun testParseWithBlankLines() {
        csvFile = tempFolder.newFile("test-blank-lines.csv")
        csvFile.writeText(
            """
            GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
            
            test-project,com.example,TestClass,10,90,5,15,3,17,2,8,1,4
            
            test-project,,Total,10,90,5,15,3,17,2,8,1,4
            
            """.trimIndent(),
        )

        val result = parser.parse(csvFile)

        assertEquals(2, result.size)
        assertEquals("TestClass", result[0].className)
        assertEquals("Total", result[1].className)
    }
}
