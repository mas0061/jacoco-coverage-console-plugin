package io.github.mas0061.jacoco

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Coverage counter data
 */
data class CoverageCounter(
    val type: String,
    val missed: Int,
    val covered: Int,
) {
    val total: Int get() = missed + covered
    val coveragePercentage: Double get() =
        if (total == 0) 0.0 else (covered.toDouble() / total) * 100
}

/**
 * Class-level coverage information
 */
data class CoverageClass(
    val name: String,
    val sourceFilename: String?,
    val counters: Map<String, CoverageCounter>,
) {
    val fullName: String get() = name.replace('/', '.')
    val instructionCoverage: Double get() = counters["INSTRUCTION"]?.coveragePercentage ?: 0.0
    val branchCoverage: Double get() = counters["BRANCH"]?.coveragePercentage ?: 0.0
}

/**
 * Package-level coverage information
 */
data class CoveragePackage(
    val name: String,
    val classes: List<CoverageClass>,
    val counters: Map<String, CoverageCounter>,
) {
    val displayName: String get() = if (name.isEmpty()) "(default package)" else name.replace('/', '.')
    val instructionCoverage: Double get() = counters["INSTRUCTION"]?.coveragePercentage ?: 0.0
    val branchCoverage: Double get() = counters["BRANCH"]?.coveragePercentage ?: 0.0
}

/**
 * Complete coverage report
 */
data class CoverageReport(
    val name: String,
    val packages: List<CoveragePackage>,
    val counters: Map<String, CoverageCounter>,
) {
    val instructionCoverage: Double get() = counters["INSTRUCTION"]?.coveragePercentage ?: 0.0
    val branchCoverage: Double get() = counters["BRANCH"]?.coveragePercentage ?: 0.0

    fun getAllClasses(): List<CoverageClass> = packages.flatMap { it.classes }
}

/**
 * JaCoCo XML report parser
 */
class XmlReportParser {
    fun parse(xmlFile: File): CoverageReport {
        validateFileAccess(xmlFile)

        val document = parseXmlDocument(xmlFile)
        val reportElement = document.documentElement

        require(reportElement.tagName == "report") {
            "Invalid JaCoCo XML format: root element should be 'report', found '${reportElement.tagName}'"
        }

        val reportName = reportElement.getAttribute("name")
        val packages = parsePackages(reportElement)
        val reportCounters = parseCounters(reportElement)

        return CoverageReport(
            name = reportName,
            packages = packages,
            counters = reportCounters,
        )
    }

    private fun validateFileAccess(xmlFile: File) {
        require(xmlFile.exists()) { "XML file not found: ${xmlFile.absolutePath}" }
        require(xmlFile.canRead()) { "XML file is not readable: ${xmlFile.absolutePath}" }
        require(xmlFile.extension.lowercase() == "xml") {
            "Expected XML file, got: ${xmlFile.name}"
        }
    }

    private fun parseXmlDocument(xmlFile: File): Document {
        return try {
            val factory = createDocumentBuilderFactory()
            val builder = factory.newDocumentBuilder()
            builder.setEntityResolver { _, _ -> null } // Ignore external entities
            builder.parse(xmlFile)
        } catch (e: IOException) {
            throw IllegalArgumentException("Failed to read XML file: ${xmlFile.name}", e)
        } catch (e: SAXException) {
            throw IllegalArgumentException("Failed to parse XML file: ${xmlFile.name}", e)
        }
    }

    private fun createDocumentBuilderFactory(): DocumentBuilderFactory {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isValidating = false
        factory.isNamespaceAware = false
        factory.setFeature("http://xml.org/sax/features/namespaces", false)
        factory.setFeature("http://xml.org/sax/features/validation", false)
        factory.setFeature(
            "http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
            false,
        )
        factory.setFeature(
            "http://apache.org/xml/features/nonvalidating/load-external-dtd",
            false,
        )
        return factory
    }

    private fun parsePackages(reportElement: Element): List<CoveragePackage> {
        val packageElements = reportElement.getElementsByTagName("package")
        return (0 until packageElements.length).map { i ->
            val packageElement = packageElements.item(i) as Element
            parsePackage(packageElement)
        }
    }

    private fun parsePackage(packageElement: Element): CoveragePackage {
        val packageName = packageElement.getAttribute("name")
        val classes = parseClasses(packageElement)
        val counters = parseCounters(packageElement)

        return CoveragePackage(
            name = packageName,
            classes = classes,
            counters = counters,
        )
    }

    private fun parseClasses(packageElement: Element): List<CoverageClass> {
        val classElements = packageElement.getElementsByTagName("class")
        return (0 until classElements.length).map { i ->
            val classElement = classElements.item(i) as Element
            parseClass(classElement)
        }
    }

    private fun parseClass(classElement: Element): CoverageClass {
        val className = classElement.getAttribute("name")
        val sourceFilename = classElement.getAttribute("sourcefilename").takeIf { it.isNotEmpty() }
        val counters = parseCounters(classElement)

        return CoverageClass(
            name = className,
            sourceFilename = sourceFilename,
            counters = counters,
        )
    }

    private fun parseCounters(element: Element): Map<String, CoverageCounter> {
        val counterElements = element.getElementsByTagName("counter")
        val counters = mutableMapOf<String, CoverageCounter>()

        for (i in 0 until counterElements.length) {
            val counterElement = counterElements.item(i) as Element

            // Only process direct children, not nested counters
            if (counterElement.parentNode == element) {
                val counter = parseCounter(counterElement)
                counters[counter.type] = counter
            }
        }

        return counters
    }

    private fun parseCounter(counterElement: Element): CoverageCounter {
        val type = counterElement.getAttribute("type")
        val missedAttr = counterElement.getAttribute("missed")
        val coveredAttr = counterElement.getAttribute("covered")
        val missed =
            missedAttr.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid missed value in counter: $missedAttr")
        val covered =
            coveredAttr.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid covered value in counter: $coveredAttr")

        return CoverageCounter(
            type = type,
            missed = missed,
            covered = covered,
        )
    }
}
