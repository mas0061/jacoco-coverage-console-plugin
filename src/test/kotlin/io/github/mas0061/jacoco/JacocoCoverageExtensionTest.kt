package io.github.mas0061.jacoco

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JacocoCoverageExtensionTest {
    @Test
    fun testExtensionExists() {
        // エクステンションクラスが存在し、必要なメソッドを持っていることを確認
        val extensionClass = JacocoCoverageExtension::class.java

        // 必要なメソッドが存在することを確認
        val csvReportPathMethod =
            extensionClass.getDeclaredMethods()
                .find { it.name.contains("csvReportPath") || it.name == "getCsvReportPath" }
        assertNotNull("csvReportPath getter should exist", csvReportPathMethod)

        val showTotalMethod =
            extensionClass.getDeclaredMethods()
                .find { it.name.contains("showTotal") || it.name == "getShowTotal" }
        assertNotNull("showTotal getter should exist", showTotalMethod)

        val targetClassesMethod =
            extensionClass.getDeclaredMethods()
                .find { it.name.contains("targetClasses") || it.name == "getTargetClasses" }
        assertNotNull("targetClasses getter should exist", targetClassesMethod)
    }

    @Test
    fun testExtensionClassStructure() {
        // エクステンションクラスが適切な構造を持っていることを確認
        val extensionClass = JacocoCoverageExtension::class.java

        // abstractクラスであることを確認
        assertTrue(
            "Extension should be abstract",
            java.lang.reflect.Modifier.isAbstract(extensionClass.modifiers),
        )

        // JacocoCoverageExtensionクラスが存在することを確認
        assertEquals("io.github.mas0061.jacoco.JacocoCoverageExtension", extensionClass.name)
    }
}
