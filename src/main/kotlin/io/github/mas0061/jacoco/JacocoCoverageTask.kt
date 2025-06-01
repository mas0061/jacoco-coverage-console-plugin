package io.github.mas0061.jacoco

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.IOException

open class JacocoCoverageTask : DefaultTask() {
    init {
        group = "reporting"
        description = "Displays JaCoCo coverage reports in console table format"
    }

    @Internal
    lateinit var extension: JacocoCoverageExtension

    @Internal
    @Option(option = "classes", description = "Comma-separated list of class names or packages to display")
    var classesOption: String = ""

    @Internal
    @Option(option = "csv-path", description = "Path to JaCoCo CSV report file")
    var csvPathOption: String = ""

    // 依存関係
    private val configManager by lazy { CoverageConfigurationManager(project, extension) }
    private val dataFilter = CoverageDataFilter()
    private val reportRenderer = CoverageReportRenderer()

    @TaskAction
    fun showCoverage() {
        try {
            val csvFile = configManager.determineCsvFile(csvPathOption)
            val parser = CsvReportParser()
            val coverageData = parser.parse(csvFile)

            val targetClasses = configManager.determineTargetClasses(classesOption)
            val showTotal = configManager.shouldShowTotal()

            val filteredData = dataFilter.filterCoverageData(coverageData, targetClasses, showTotal)

            if (filteredData.isEmpty()) {
                println("No coverage data found for the specified criteria.")
                return
            }

            reportRenderer.renderCoverageTable(filteredData)
        } catch (e: IOException) {
            logger.error("Failed to read coverage report file: ${e.message}", e)
            throw e
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid coverage report: ${e.message}", e)
            throw e
        }
    }
}
