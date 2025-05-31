package io.github.mas0061.jacoco

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoverageDataFilterTest {
    private val filter = CoverageDataFilter()

    private val sampleData =
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
                packageName = "com.example.model",
                className = "User",
                instructionMissed = 5,
                instructionCovered = 45,
                branchMissed = 2,
                branchCovered = 8,
                lineMissed = 1,
                lineCovered = 9,
                complexityMissed = 0,
                complexityCovered = 5,
                methodMissed = 0,
                methodCovered = 3,
            ),
            CoverageRow(
                group = "test-project",
                packageName = "",
                className = "Total",
                instructionMissed = 15,
                instructionCovered = 135,
                branchMissed = 7,
                branchCovered = 23,
                lineMissed = 3,
                lineCovered = 27,
                complexityMissed = 1,
                complexityCovered = 14,
                methodMissed = 0,
                methodCovered = 8,
            ),
        )

    @Test
    fun testFilterWithNoTargetsAndShowTotal() {
        val result = filter.filterCoverageData(sampleData, emptyList(), true)

        assertEquals(1, result.size)
        assertEquals("Total", result[0].className)
    }

    @Test
    fun testFilterWithNoTargetsAndHideTotal() {
        val result = filter.filterCoverageData(sampleData, emptyList(), false)

        assertEquals(3, result.size)
        assertEquals(sampleData, result)
    }

    @Test
    fun testFilterWithExactClassMatch() {
        val targetClasses = listOf("com.example.service.UserService")
        val result = filter.filterCoverageData(sampleData, targetClasses, false)

        assertEquals(1, result.size)
        assertEquals("UserService", result[0].className)
    }

    @Test
    fun testFilterWithPackageWildcard() {
        val targetClasses = listOf("com.example.service.*")
        val result = filter.filterCoverageData(sampleData, targetClasses, false)

        assertEquals(1, result.size)
        assertEquals("UserService", result[0].className)
    }

    @Test
    fun testFilterWithPackageMatch() {
        val targetClasses = listOf("com.example.model")
        val result = filter.filterCoverageData(sampleData, targetClasses, false)

        assertEquals(1, result.size)
        assertEquals("User", result[0].className)
    }

    @Test
    fun testFilterWithMultipleTargets() {
        val targetClasses = listOf("com.example.service.*", "com.example.model.User")
        val result = filter.filterCoverageData(sampleData, targetClasses, false)

        assertEquals(2, result.size)
        assertTrue(result.any { it.className == "UserService" })
        assertTrue(result.any { it.className == "User" })
    }

    @Test
    fun testFilterWithNoMatches() {
        val targetClasses = listOf("com.nonexistent.*")
        val result = filter.filterCoverageData(sampleData, targetClasses, false)

        assertEquals(0, result.size)
    }
}
