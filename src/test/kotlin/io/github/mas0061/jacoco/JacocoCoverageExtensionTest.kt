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

        // 必要なフィールドが存在することを確認
        val csvReportPathField = extensionClass.getDeclaredField("csvReportPath")
        assertNotNull("csvReportPath field should exist", csvReportPathField)

        val showTotalField = extensionClass.getDeclaredField("showTotal")
        assertNotNull("showTotal field should exist", showTotalField)

        val targetClassesField = extensionClass.getDeclaredField("targetClasses")
        assertNotNull("targetClasses field should exist", targetClassesField)
    }

    @Test
    fun testExtensionClassStructure() {
        // エクステンションクラスが適切な構造を持っていることを確認
        val extensionClass = JacocoCoverageExtension::class.java

        // openクラスであることを確認（Gradle 4との互換性のためabstractからopenに変更）
        assertTrue(
            "Extension should be open class",
            !java.lang.reflect.Modifier.isAbstract(extensionClass.modifiers) &&
                !java.lang.reflect.Modifier.isFinal(extensionClass.modifiers),
        )

        // JacocoCoverageExtensionクラスが存在することを確認
        assertEquals("io.github.mas0061.jacoco.JacocoCoverageExtension", extensionClass.name)
    }
}
