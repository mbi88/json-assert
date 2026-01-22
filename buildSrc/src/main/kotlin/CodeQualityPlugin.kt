import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.PmdExtension

class CodeQualityPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("checkstyle")
            pluginManager.apply("pmd")

            val checkstyle = extensions.getByType(CheckstyleExtension::class.java)
            checkstyle.toolVersion = "10.16.0"
            checkstyle.configFile = file("config/checkstyle/checkstyle.xml")
            checkstyle.isShowViolations = true

            val pmd = extensions.getByType(PmdExtension::class.java)
            pmd.toolVersion = "7.12.0"
            pmd.isConsoleOutput = true
            pmd.ruleSets = listOf()
            pmd.ruleSetFiles = files("config/pmd/pmd-ruleset.xml")
        }
    }
}
