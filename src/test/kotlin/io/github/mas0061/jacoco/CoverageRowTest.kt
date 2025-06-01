package io.github.mas0061.jacoco

import org.junit.Assert.assertEquals
import org.junit.Test

class CoverageRowTest {
    @Test
    fun testCoverageCalculations() {
        val row =
            CoverageRow(
                group = "test-group",
                packageName = "com.example",
                className = "TestClass",
                instructionMissed = 20,
                instructionCovered = 80,
                branchMissed = 10,
                branchCovered = 30,
                lineMissed = 5,
                lineCovered = 15,
                complexityMissed = 3,
                complexityCovered = 7,
                methodMissed = 2,
                methodCovered = 8,
            )

        assertEquals(100, row.instructionTotal)
        assertEquals(40, row.branchTotal)
        assertEquals(80.0, row.instructionCoverage, 0.01)
        assertEquals(75.0, row.branchCoverage, 0.01)
        assertEquals("com.example.TestClass", row.fullClassName)
    }

    @Test
    fun testCoverageCalculationsWithZeroTotals() {
        val row =
            CoverageRow(
                group = "test-group",
                packageName = "com.example",
                className = "EmptyClass",
                instructionMissed = 0,
                instructionCovered = 0,
                branchMissed = 0,
                branchCovered = 0,
                lineMissed = 0,
                lineCovered = 0,
                complexityMissed = 0,
                complexityCovered = 0,
                methodMissed = 0,
                methodCovered = 0,
            )

        assertEquals(0, row.instructionTotal)
        assertEquals(0, row.branchTotal)
        assertEquals(0.0, row.instructionCoverage, 0.01)
        assertEquals(0.0, row.branchCoverage, 0.01)
    }

    @Test
    fun testFullClassNameWithEmptyPackage() {
        val row =
            CoverageRow(
                group = "test-group",
                packageName = "",
                className = "RootClass",
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
            )

        assertEquals("RootClass", row.fullClassName)
    }

    @Test
    fun testTotalRow() {
        val totalRow =
            CoverageRow(
                group = "test-group",
                packageName = "",
                className = "Total",
                instructionMissed = 50,
                instructionCovered = 450,
                branchMissed = 25,
                branchCovered = 75,
                lineMissed = 15,
                lineCovered = 85,
                complexityMissed = 10,
                complexityCovered = 40,
                methodMissed = 5,
                methodCovered = 25,
            )

        assertEquals(500, totalRow.instructionTotal)
        assertEquals(100, totalRow.branchTotal)
        assertEquals(90.0, totalRow.instructionCoverage, 0.01)
        assertEquals(75.0, totalRow.branchCoverage, 0.01)
        assertEquals("Total", totalRow.fullClassName)
    }

    @Test
    fun testPerfectCoverage() {
        val row =
            CoverageRow(
                group = "test-group",
                packageName = "com.example",
                className = "PerfectClass",
                instructionMissed = 0,
                instructionCovered = 100,
                branchMissed = 0,
                branchCovered = 20,
                lineMissed = 0,
                lineCovered = 25,
                complexityMissed = 0,
                complexityCovered = 10,
                methodMissed = 0,
                methodCovered = 5,
            )

        assertEquals(100.0, row.instructionCoverage, 0.01)
        assertEquals(100.0, row.branchCoverage, 0.01)
    }
}
