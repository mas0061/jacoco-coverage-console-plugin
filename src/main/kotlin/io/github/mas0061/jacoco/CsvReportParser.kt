package io.github.mas0061.jacoco

/**
 * JaCoCoのCSVレポートの1行を表すデータクラス
 */
data class CoverageRow(
    val group: String,
    val packageName: String,
    val className: String,
    val instructionMissed: Int,
    val instructionCovered: Int,
    val branchMissed: Int,
    val branchCovered: Int,
    val lineMissed: Int,
    val lineCovered: Int,
    val complexityMissed: Int,
    val complexityCovered: Int,
    val methodMissed: Int,
    val methodCovered: Int,
) {
    val instructionTotal: Int get() = instructionMissed + instructionCovered
    val branchTotal: Int get() = branchMissed + branchCovered

    val instructionCoverage: Double get() =
        if (instructionTotal == 0) 0.0 else (instructionCovered.toDouble() / instructionTotal) * 100

    val branchCoverage: Double get() =
        if (branchTotal == 0) 0.0 else (branchCovered.toDouble() / branchTotal) * 100

    val fullClassName: String get() = if (packageName.isNotEmpty()) "$packageName.$className" else className
}

/**
 * JaCoCoのCSVレポートをパースするクラス
 */
class CsvReportParser {
    companion object {
        private const val EXPECTED_COLUMN_COUNT = 13
        private val EXPECTED_HEADERS =
            listOf(
                "GROUP", "PACKAGE", "CLASS", "INSTRUCTION_MISSED", "INSTRUCTION_COVERED",
                "BRANCH_MISSED", "BRANCH_COVERED", "LINE_MISSED", "LINE_COVERED",
                "COMPLEXITY_MISSED", "COMPLEXITY_COVERED", "METHOD_MISSED", "METHOD_COVERED",
            )
    }

    fun parse(csvFile: java.io.File): List<CoverageRow> {
        validateFileAccess(csvFile)

        val lines = csvFile.readLines()
        require(lines.isNotEmpty()) { "CSV file is empty: ${csvFile.absolutePath}" }

        validateHeader(lines.first())

        return lines
            .drop(1) // ヘッダー行をスキップ
            .filter { it.isNotBlank() }
            .mapIndexed { index, line ->
                try {
                    parseLine(line)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException(
                        "Failed to parse line ${index + 2} in ${csvFile.name}: ${e.message}",
                        e,
                    )
                }
            }
    }

    private fun validateFileAccess(csvFile: java.io.File) {
        require(csvFile.exists()) { "CSV file not found: ${csvFile.absolutePath}" }
        require(csvFile.canRead()) { "CSV file is not readable: ${csvFile.absolutePath}" }
    }

    private fun validateHeader(headerLine: String) {
        val headers = parseCSVLine(headerLine)
        require(headers.size == EXPECTED_COLUMN_COUNT) {
            "Invalid header format: expected $EXPECTED_COLUMN_COUNT columns, got ${headers.size}"
        }

        headers.forEachIndexed { index, header ->
            require(header.trim() == EXPECTED_HEADERS[index]) {
                "Invalid header at column ${index + 1}: expected '${EXPECTED_HEADERS[index]}', got '$header'"
            }
        }
    }

    private fun parseLine(line: String): CoverageRow {
        val columns = parseCSVLine(line)
        require(columns.size >= EXPECTED_COLUMN_COUNT) {
            "Invalid CSV format: expected $EXPECTED_COLUMN_COUNT columns, got ${columns.size}"
        }

        return CoverageRow(
            group = columns[0].trim(),
            packageName = columns[1].trim(),
            className = columns[2].trim(),
            instructionMissed = parseIntColumn(columns[3], "INSTRUCTION_MISSED"),
            instructionCovered = parseIntColumn(columns[4], "INSTRUCTION_COVERED"),
            branchMissed = parseIntColumn(columns[5], "BRANCH_MISSED"),
            branchCovered = parseIntColumn(columns[6], "BRANCH_COVERED"),
            lineMissed = parseIntColumn(columns[7], "LINE_MISSED"),
            lineCovered = parseIntColumn(columns[8], "LINE_COVERED"),
            complexityMissed = parseIntColumn(columns[9], "COMPLEXITY_MISSED"),
            complexityCovered = parseIntColumn(columns[10], "COMPLEXITY_COVERED"),
            methodMissed = parseIntColumn(columns[11], "METHOD_MISSED"),
            methodCovered = parseIntColumn(columns[12], "METHOD_COVERED"),
        )
    }

    /**
     * CSVの行をパースする。カンマで区切られた値を考慮し、クォートされた値も適切に処理する
     */
    private fun parseCSVLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < line.length) {
            val char = line[i]
            when {
                char == '"' && !inQuotes -> {
                    inQuotes = true
                }
                char == '"' && inQuotes -> {
                    // Check if it's an escaped quote
                    if (i + 1 < line.length && line[i + 1] == '"') {
                        current.append('"')
                        i++ // Skip the next quote
                    } else {
                        inQuotes = false
                    }
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }
                else -> {
                    current.append(char)
                }
            }
            i++
        }

        // Add the last field
        result.add(current.toString())

        return result
    }

    private fun parseIntColumn(
        value: String,
        columnName: String,
    ): Int {
        return value.trim().toIntOrNull()
            ?: throw NumberFormatException("Invalid integer value for $columnName: '$value'")
    }
}
