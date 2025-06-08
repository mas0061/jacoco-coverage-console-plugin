package io.github.mas0061.jacoco

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.IOException

open class JacocoCoverageTask : DefaultTask() {
    init {
        group = "reporting"
        description = "Displays JaCoCo coverage reports in console table format with " +
            "project totals and package summaries"
    }

    @Internal
    lateinit var extension: JacocoCoverageExtension

    @Internal
    @Option(option = "classes", description = "Comma-separated list of class names or packages to display")
    var classesOption: String = ""

    @Internal
    @Option(option = "xml-path", description = "Path to JaCoCo XML report file")
    var xmlPathOption: String = ""

    @Internal
    @Option(option = "csv-path", description = "Path to JaCoCo CSV report file (deprecated, use xml-path)")
    var csvPathOption: String = ""

    // 依存関係
    private val configManager by lazy { CoverageConfigurationManager(project, extension) }
    private val dataFilter = CoverageDataFilter()
    private val csvReportRenderer = CoverageReportRenderer()
    private val xmlReportRenderer = XmlCoverageReportRenderer()

    @TaskAction
    fun showCoverage() {
        try {
            val targetClasses = configManager.determineTargetClasses(classesOption)

            if (shouldUseXmlReport()) {
                renderXmlReport(targetClasses)
            } else {
                renderCsvReport(targetClasses)
            }
        } catch (e: IOException) {
            logger.error("Failed to read coverage report file: ${e.message}", e)
            throw e
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid coverage report: ${e.message}", e)
            throw e
        }
    }

    private fun shouldUseXmlReport(): Boolean {
        val csvExplicitlySpecified =
            csvPathOption.isNotEmpty() ||
                project.hasProperty("jacocoCsvPath") ||
                extension.csvReportPath != null

        return xmlPathOption.isNotEmpty() ||
            project.hasProperty("jacocoXmlPath") ||
            !csvExplicitlySpecified
    }

    private fun renderXmlReport(targetClasses: List<String>) {
        val xmlFile = configManager.determineXmlFile(xmlPathOption)
        val parser = XmlReportParser()
        val report = parser.parse(xmlFile)

        if (report.packages.isEmpty() || report.getAllClasses().isEmpty()) {
            println("No coverage data found in the XML report.")
            return
        }

        xmlReportRenderer.renderCoverageReport(
            report = report,
            targetClasses = targetClasses,
            showTotal = extension.showTotal,
            showPackageSummary = extension.showPackageSummary,
        )
    }

    private fun renderCsvReport(targetClasses: List<String>) {
        logger.warn(
            "CSV reports are deprecated. Consider using XML reports for " +
                "better functionality including project totals.",
        )

        val csvFile = configManager.determineCsvFile(csvPathOption)
        val parser = CsvReportParser()
        val coverageData = parser.parse(csvFile)

        val showTotal = configManager.shouldShowTotal()
        val filteredData = dataFilter.filterCoverageData(coverageData, targetClasses, showTotal)

        if (filteredData.isEmpty()) {
            println("No coverage data found for the specified criteria.")
            return
        }

        csvReportRenderer.renderCoverageTable(filteredData)
    }
}
