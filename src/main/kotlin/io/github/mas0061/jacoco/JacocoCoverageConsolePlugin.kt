package io.github.mas0061.jacoco

import org.gradle.api.Plugin
import org.gradle.api.Project

class JacocoCoverageConsolePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("jacocoCoverageConsole", JacocoCoverageExtension::class.java)

        project.tasks.register("jacocoCoverageConsole", JacocoCoverageTask::class.java).configure {
            this.extension = extension
        }
    }
}
