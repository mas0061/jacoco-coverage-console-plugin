package io.github.mas0061.jacoco

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class XmlReportParserTest {
    @Rule
    @JvmField
    val tempFolder = TemporaryFolder()

    private lateinit var xmlReportParser: XmlReportParser

    @Before
    fun setUp() {
        xmlReportParser = XmlReportParser()
    }

    @Test
    fun testParseValidXmlReport() {
        val xmlFile = createValidJacocoXmlFile()

        val result = xmlReportParser.parse(xmlFile)

        // Test report level
        assertEquals("test-project", result.name)
        assertEquals(85.0, result.instructionCoverage, 0.01)
        assertEquals(75.0, result.branchCoverage, 0.01)

        // Test report counters
        assertEquals(30, result.counters["INSTRUCTION"]?.missed)
        assertEquals(170, result.counters["INSTRUCTION"]?.covered)
        assertEquals(200, result.counters["INSTRUCTION"]?.total)
        assertEquals(10, result.counters["BRANCH"]?.missed)
        assertEquals(30, result.counters["BRANCH"]?.covered)

        // Test packages
        assertEquals(2, result.packages.size)

        // Test first package (com.example)
        val examplePackage = result.packages[0]
        assertEquals("com.example", examplePackage.name)
        assertEquals("com.example", examplePackage.displayName)
        assertEquals(90.0, examplePackage.instructionCoverage, 0.01)
        assertEquals(75.0, examplePackage.branchCoverage, 0.01)

        // Test second package (default package)
        val defaultPackage = result.packages[1]
        assertEquals("", defaultPackage.name)
        assertEquals("(default package)", defaultPackage.displayName)
        assertEquals(80.0, defaultPackage.instructionCoverage, 0.01)
        assertEquals(75.0, defaultPackage.branchCoverage, 0.01)

        // Test classes in first package
        assertEquals(2, examplePackage.classes.size)

        val userService = examplePackage.classes[0]
        assertEquals("com/example/UserService", userService.name)
        assertEquals("com.example.UserService", userService.fullName)
        assertEquals("UserService.java", userService.sourceFilename)
        assertEquals(90.0, userService.instructionCoverage, 0.01)
        assertEquals(80.0, userService.branchCoverage, 0.01)

        val userModel = examplePackage.classes[1]
        assertEquals("com/example/User", userModel.name)
        assertEquals("com.example.User", userModel.fullName)
        assertEquals("User.java", userModel.sourceFilename)
        assertEquals(90.0, userModel.instructionCoverage, 0.01)
        assertEquals(66.67, userModel.branchCoverage, 0.01)

        // Test getAllClasses method
        val allClasses = result.getAllClasses()
        assertEquals(3, allClasses.size)
        assertEquals("com.example.UserService", allClasses[0].fullName)
        assertEquals("com.example.User", allClasses[1].fullName)
        assertEquals("RootClass", allClasses[2].fullName)
    }

    @Test
    fun testParseXmlWithNoSourceFilename() {
        val xmlFile = createXmlFileWithoutSourceFilename()

        val result = xmlReportParser.parse(xmlFile)

        val testClass = result.packages[0].classes[0]
        assertEquals("com/example/TestClass", testClass.name)
        assertNull(testClass.sourceFilename)
    }

    @Test
    fun testCoverageCounterCalculations() {
        val counter = CoverageCounter("INSTRUCTION", 20, 80)

        assertEquals(100, counter.total)
        assertEquals(80.0, counter.coveragePercentage, 0.01)
    }

    @Test
    fun testCoverageCounterZeroTotal() {
        val counter = CoverageCounter("INSTRUCTION", 0, 0)

        assertEquals(0, counter.total)
        assertEquals(0.0, counter.coveragePercentage, 0.01)
    }

    @Test
    fun testCoverageClassDataModelFunctions() {
        val counters =
            mapOf(
                "INSTRUCTION" to CoverageCounter("INSTRUCTION", 10, 90),
                "BRANCH" to CoverageCounter("BRANCH", 5, 15),
            )

        val coverageClass =
            CoverageClass(
                name = "com/example/TestClass",
                sourceFilename = "TestClass.java",
                counters = counters,
            )

        assertEquals("com.example.TestClass", coverageClass.fullName)
        assertEquals(90.0, coverageClass.instructionCoverage, 0.01)
        assertEquals(75.0, coverageClass.branchCoverage, 0.01)
    }

    @Test
    fun testCoverageClassWithMissingCounters() {
        val coverageClass =
            CoverageClass(
                name = "com/example/TestClass",
                sourceFilename = null,
                counters = emptyMap(),
            )

        assertEquals(0.0, coverageClass.instructionCoverage, 0.01)
        assertEquals(0.0, coverageClass.branchCoverage, 0.01)
    }

    @Test
    fun testCoveragePackageDataModelFunctions() {
        val counters =
            mapOf(
                "INSTRUCTION" to CoverageCounter("INSTRUCTION", 20, 80),
                "BRANCH" to CoverageCounter("BRANCH", 10, 30),
            )

        val coveragePackage =
            CoveragePackage(
                name = "com/example/service",
                classes = emptyList(),
                counters = counters,
            )

        assertEquals("com.example.service", coveragePackage.displayName)
        assertEquals(80.0, coveragePackage.instructionCoverage, 0.01)
        assertEquals(75.0, coveragePackage.branchCoverage, 0.01)
    }

    @Test
    fun testCoveragePackageDefaultPackage() {
        val coveragePackage =
            CoveragePackage(
                name = "",
                classes = emptyList(),
                counters = emptyMap(),
            )

        assertEquals("(default package)", coveragePackage.displayName)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseNonExistentFile() {
        val nonExistentFile = File(tempFolder.root, "non-existent.xml")
        xmlReportParser.parse(nonExistentFile)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseNonXmlFile() {
        val txtFile = tempFolder.newFile("test.txt")
        txtFile.writeText("This is not an XML file")
        xmlReportParser.parse(txtFile)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseInvalidXmlFormat() {
        val xmlFile = tempFolder.newFile("invalid.xml")
        xmlFile.writeText("This is not valid XML content")
        xmlReportParser.parse(xmlFile)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseXmlWithWrongRootElement() {
        val xmlFile = tempFolder.newFile("wrong-root.xml")
        xmlFile.writeText(
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <notareport name="test">
            </notareport>
            """.trimIndent(),
        )
        xmlReportParser.parse(xmlFile)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseXmlWithInvalidCounterValues() {
        val xmlFile = tempFolder.newFile("invalid-counter.xml")
        xmlFile.writeText(
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <report name="test-project">
                <package name="com.example">
                    <class name="com/example/TestClass" sourcefilename="TestClass.java">
                        <counter type="INSTRUCTION" missed="invalid" covered="90"/>
                    </class>
                </package>
            </report>
            """.trimIndent(),
        )
        xmlReportParser.parse(xmlFile)
    }

    @Test
    fun testParseEmptyXmlReport() {
        val xmlFile = tempFolder.newFile("empty-report.xml")
        xmlFile.writeText(
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <report name="empty-project">
                <counter type="INSTRUCTION" missed="0" covered="0"/>
                <counter type="BRANCH" missed="0" covered="0"/>
            </report>
            """.trimIndent(),
        )

        val result = xmlReportParser.parse(xmlFile)

        assertEquals("empty-project", result.name)
        assertTrue(result.packages.isEmpty())
        assertEquals(0.0, result.instructionCoverage, 0.01)
        assertEquals(0.0, result.branchCoverage, 0.01)
    }

    @Test
    fun testParseXmlWithNestedCounters() {
        val xmlFile = tempFolder.newFile("nested-counters.xml")
        xmlFile.writeText(
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <report name="test-project">
                <counter type="INSTRUCTION" missed="30" covered="170"/>
                <counter type="BRANCH" missed="10" covered="30"/>
                <package name="com.example">
                    <counter type="INSTRUCTION" missed="20" covered="80"/>
                    <counter type="BRANCH" missed="5" covered="15"/>
                    <class name="com/example/TestClass" sourcefilename="TestClass.java">
                        <counter type="INSTRUCTION" missed="10" covered="40"/>
                        <counter type="BRANCH" missed="2" covered="8"/>
                        <method name="testMethod" desc="()V" line="10">
                            <counter type="INSTRUCTION" missed="5" covered="20"/>
                        </method>
                    </class>
                </package>
            </report>
            """.trimIndent(),
        )

        val result = xmlReportParser.parse(xmlFile)

        // Verify that nested counters (in methods) are not parsed into class counters
        val testClass = result.packages[0].classes[0]
        assertEquals(2, testClass.counters.size) // Only class-level counters
        assertEquals(10, testClass.counters["INSTRUCTION"]?.missed)
        assertEquals(40, testClass.counters["INSTRUCTION"]?.covered)
    }

    private fun createValidJacocoXmlFile(): File {
        val xmlFile = tempFolder.newFile("jacoco-report.xml")
        xmlFile.writeText(createSampleXmlContent())
        return xmlFile
    }

    private fun createSampleXmlContent(): String {
        return """<?xml version="1.0" encoding="UTF-8"?>
<report name="test-project">
    $REPORT_COUNTERS
    $PACKAGE_CONTENT
    $DEFAULT_PACKAGE_CONTENT
</report>"""
    }

    companion object {
        private const val REPORT_COUNTERS = """    <counter type="INSTRUCTION" missed="30" covered="170"/>
    <counter type="BRANCH" missed="10" covered="30"/>
    <counter type="LINE" missed="15" covered="85"/>
    <counter type="COMPLEXITY" missed="8" covered="32"/>
    <counter type="METHOD" missed="3" covered="17"/>
    <counter type="CLASS" missed="1" covered="4"/>"""

        private const val PACKAGE_CONTENT = """    <package name="com.example">
        <counter type="INSTRUCTION" missed="10" covered="90"/>
        <counter type="BRANCH" missed="5" covered="15"/>
        <counter type="LINE" missed="5" covered="45"/>
        <counter type="COMPLEXITY" missed="3" covered="17"/>
        <counter type="METHOD" missed="1" covered="9"/>
        <counter type="CLASS" missed="0" covered="2"/>
        
        <class name="com/example/UserService" sourcefilename="UserService.java">
            <counter type="INSTRUCTION" missed="5" covered="45"/>
            <counter type="BRANCH" missed="2" covered="8"/>
            <counter type="LINE" missed="2" covered="23"/>
            <counter type="COMPLEXITY" missed="1" covered="9"/>
            <counter type="METHOD" missed="0" covered="5"/>
            <counter type="CLASS" missed="0" covered="1"/>
        </class>
        
        <class name="com/example/User" sourcefilename="User.java">
            <counter type="INSTRUCTION" missed="5" covered="45"/>
            <counter type="BRANCH" missed="3" covered="6"/>
            <counter type="LINE" missed="3" covered="22"/>
            <counter type="COMPLEXITY" missed="2" covered="8"/>
            <counter type="METHOD" missed="1" covered="4"/>
            <counter type="CLASS" missed="0" covered="1"/>
        </class>
    </package>"""

        private const val DEFAULT_PACKAGE_CONTENT = """    <package name="">
        <counter type="INSTRUCTION" missed="20" covered="80"/>
        <counter type="BRANCH" missed="5" covered="15"/>
        <counter type="LINE" missed="10" covered="40"/>
        <counter type="COMPLEXITY" missed="5" covered="15"/>
        <counter type="METHOD" missed="2" covered="8"/>
        <counter type="CLASS" missed="1" covered="2"/>
        
        <class name="RootClass" sourcefilename="RootClass.java">
            <counter type="INSTRUCTION" missed="20" covered="80"/>
            <counter type="BRANCH" missed="5" covered="15"/>
            <counter type="LINE" missed="10" covered="40"/>
            <counter type="COMPLEXITY" missed="5" covered="15"/>
            <counter type="METHOD" missed="2" covered="8"/>
            <counter type="CLASS" missed="1" covered="2"/>
        </class>
    </package>"""
    }

    private fun createXmlFileWithoutSourceFilename(): File {
        val xmlFile = tempFolder.newFile("no-source-filename.xml")
        xmlFile.writeText(
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <report name="test-project">
                <counter type="INSTRUCTION" missed="10" covered="90"/>
                <package name="com.example">
                    <counter type="INSTRUCTION" missed="10" covered="90"/>
                    <class name="com/example/TestClass">
                        <counter type="INSTRUCTION" missed="10" covered="90"/>
                    </class>
                </package>
            </report>
            """.trimIndent(),
        )
        return xmlFile
    }
}
