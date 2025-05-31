package io.github.mas0061.jacoco

/**
 * カバレッジデータのフィルタリングを担当するクラス
 */
class CoverageDataFilter {
    /**
     * 指定された条件に基づいてカバレッジデータをフィルタリングする
     */
    fun filterCoverageData(
        data: List<CoverageRow>,
        targetClasses: List<String>,
        showTotal: Boolean,
    ): List<CoverageRow> {
        return when {
            targetClasses.isEmpty() && showTotal -> {
                // Totalの行のみを返す
                data.filter { it.className == "Total" }
            }
            targetClasses.isEmpty() -> {
                data
            }
            else -> {
                data.filter { row -> matchesTargetClasses(row, targetClasses) }
            }
        }
    }

    /**
     * 行が指定されたターゲットクラス/パッケージにマッチするかを判定する
     */
    private fun matchesTargetClasses(
        row: CoverageRow,
        targetClasses: List<String>,
    ): Boolean {
        return targetClasses.any { target ->
            when {
                // パッケージ指定（末尾に*がある場合）
                target.endsWith("*") -> {
                    val packagePrefix = target.dropLast(1)
                    row.fullClassName.startsWith(packagePrefix) ||
                        row.packageName.startsWith(packagePrefix)
                }
                // 完全一致
                else -> row.fullClassName == target || row.packageName == target
            }
        }
    }
}
