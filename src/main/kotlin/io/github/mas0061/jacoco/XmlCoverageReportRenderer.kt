package io.github.mas0061.jacoco

import java.util.Locale

/**
 * XML-based coverage report renderer with support for project totals and package summaries
 */
class XmlCoverageReportRenderer {
    companion object {
        private const val TABLE_WIDTH = 82 // 50 + 1 + 15 + 1 + 15 = 82
        private const val CLASS_COLUMN_WIDTH = 50
        private const val COVERAGE_COLUMN_WIDTH = 15
    }

    /**
     * Render coverage report with optional filtering and package grouping
     */
    fun renderCoverageReport(
        report: CoverageReport,
        targetClasses: List<String> = emptyList(),
        showTotal: Boolean = true,
        showPackageSummary: Boolean = true,
    ) {
        val filteredData =
            if (targetClasses.isEmpty()) {
                report
            } else {
                filterReport(report, targetClasses)
            }

        printHeader()
        printTableHeader()

        if (showTotal) {
            printProjectTotal(filteredData)
        }

        if (showPackageSummary && filteredData.packages.size > 1) {
            printPackageSummaries(filteredData)
        }

        printClassDetails(filteredData)
        printFooter()
    }

    private fun filterReport(
        report: CoverageReport,
        targetClasses: List<String>,
    ): CoverageReport {
        val filteredPackages = mutableListOf<CoveragePackage>()

        for (pkg in report.packages) {
            val filteredClasses =
                pkg.classes.filter { clazz ->
                    targetClasses.any { pattern ->
                        matchesPattern(clazz.fullName, pattern) ||
                            matchesPattern(pkg.displayName, pattern)
                    }
                }

            if (filteredClasses.isNotEmpty()) {
                // Recalculate package counters based on filtered classes
                val packageCounters = aggregateCounters(filteredClasses.map { it.counters })
                filteredPackages.add(
                    pkg.copy(
                        classes = filteredClasses,
                        counters = packageCounters,
                    ),
                )
            }
        }

        // Recalculate report counters based on filtered packages
        val reportCounters = aggregateCounters(filteredPackages.map { it.counters })

        return report.copy(
            packages = filteredPackages,
            counters = reportCounters,
        )
    }

    private fun matchesPattern(
        name: String,
        pattern: String,
    ): Boolean {
        return when {
            pattern.endsWith("*") -> {
                val prefix = pattern.dropLast(1)
                name.startsWith(prefix)
            }
            pattern.startsWith("*") -> {
                val suffix = pattern.drop(1)
                name.endsWith(suffix)
            }
            pattern.contains("*") -> {
                val parts = pattern.split("*")
                if (parts.size == 2) {
                    name.startsWith(parts[0]) && name.endsWith(parts[1])
                } else {
                    false
                }
            }
            else -> name == pattern
        }
    }

    private fun aggregateCounters(countersList: List<Map<String, CoverageCounter>>): Map<String, CoverageCounter> {
        val aggregated = mutableMapOf<String, Pair<Int, Int>>()

        for (counters in countersList) {
            for ((type, counter) in counters) {
                val (currentMissed, currentCovered) = aggregated.getOrDefault(type, 0 to 0)
                aggregated[type] = (currentMissed + counter.missed) to (currentCovered + counter.covered)
            }
        }

        return aggregated.mapValues { (type, counts) ->
            CoverageCounter(type, counts.first, counts.second)
        }
    }

    private fun printHeader() {
        println()
        println("=".repeat(TABLE_WIDTH))
        println("JaCoCo Coverage Report")
        println("=".repeat(TABLE_WIDTH))
    }

    private fun printTableHeader() {
        val header =
            String.format(
                Locale.US,
                "%-${CLASS_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s",
                "Class/Package",
                "Instruction (%)",
                "Branch (%)",
            )
        println(header)
        println("-".repeat(TABLE_WIDTH))
    }

    private fun printProjectTotal(report: CoverageReport) {
        val instructionCoverage = String.format(Locale.US, "%.2f", report.instructionCoverage)
        val branchCoverage = String.format(Locale.US, "%.2f", report.branchCoverage)

        val totalRow =
            String.format(
                Locale.US,
                "%-${CLASS_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s",
                "PROJECT TOTAL",
                instructionCoverage,
                branchCoverage,
            )
        println(totalRow)
    }

    private fun printPackageSummaries(report: CoverageReport) {
        for (pkg in report.packages.sortedBy { it.displayName }) {
            val instructionCoverage = String.format(Locale.US, "%.2f", pkg.instructionCoverage)
            val branchCoverage = String.format(Locale.US, "%.2f", pkg.branchCoverage)

            val packageRow =
                String.format(
                    Locale.US,
                    "%-${CLASS_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s",
                    "${pkg.displayName} (package)",
                    instructionCoverage,
                    branchCoverage,
                )
            println(packageRow)
        }
    }

    private fun printClassDetails(report: CoverageReport) {
        val allClasses = report.getAllClasses().sortedBy { it.fullName }

        for (clazz in allClasses) {
            val instructionCoverage = String.format(Locale.US, "%.2f", clazz.instructionCoverage)
            val branchCoverage = String.format(Locale.US, "%.2f", clazz.branchCoverage)

            val classRow =
                String.format(
                    Locale.US,
                    "%-${CLASS_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s",
                    "  ${clazz.fullName}",
                    instructionCoverage,
                    branchCoverage,
                )
            println(classRow)
        }
    }

    private fun printFooter() {
        println("-".repeat(TABLE_WIDTH))
        println()
    }
}
