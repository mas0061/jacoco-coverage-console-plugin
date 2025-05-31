package io.github.mas0061.jacoco

import java.util.Locale

/**
 * カバレッジレポートの表示を担当するクラス
 */
class CoverageReportRenderer {
    companion object {
        private const val TABLE_WIDTH = 82 // 50 + 1 + 15 + 1 + 15 = 82
        private const val CLASS_COLUMN_WIDTH = 50
        private const val COVERAGE_COLUMN_WIDTH = 15
        private const val TOTAL_CLASS_NAME = "Total"
    }

    /**
     * カバレッジデータをコンソールテーブル形式で表示する
     */
    fun renderCoverageTable(data: List<CoverageRow>) {
        val sortedData = data.sortedBy { it.fullClassName }

        printHeader()
        printTableHeader()
        printDataRows(sortedData)
        printFooter()
    }

    private fun printHeader() {
        println()
        println("=".repeat(TABLE_WIDTH))
        println("JaCoCo Coverage Report")
        println("=".repeat(TABLE_WIDTH))
        println()
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

    private fun printDataRows(data: List<CoverageRow>) {
        for (row in data) {
            val displayName = if (row.className == TOTAL_CLASS_NAME) "TOTAL" else row.fullClassName
            val instructionCoverage = String.format(Locale.US, "%.2f", row.instructionCoverage)
            val branchCoverage = String.format(Locale.US, "%.2f", row.branchCoverage)

            val dataRow =
                String.format(
                    Locale.US,
                    "%-${CLASS_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s %${COVERAGE_COLUMN_WIDTH}s",
                    displayName,
                    instructionCoverage,
                    branchCoverage,
                )
            println(dataRow)
        }
    }

    private fun printFooter() {
        println("-".repeat(TABLE_WIDTH))
        println()
    }
}
