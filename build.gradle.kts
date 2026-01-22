import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsReport
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

plugins {
    id("java-library")
    id("jacoco")
    id("maven-publish")
    id("code-quality")
    id("com.github.spotbugs") version "6.4.8"
}

group = "com.mbi"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.json:json:20251224")
    implementation("org.skyscreamer:jsonassert:1.5.3")
    implementation("org.testng:testng:7.11.0")
    implementation("io.rest-assured:rest-assured:6.0.0")
    implementation("com.github.wnameless.json:json-flattener:0.18.0")
}

tasks.test {
    useTestNG {
        // Automatically include all XML test suite files from suitesDir
        val suitesDir = "src/test/resources/suites/"
        fileTree(suitesDir).matching { include("*.xml") }.files.forEach { suites(it) }
    }

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/coverage"))
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
}

spotbugs {
    toolVersion.set("4.9.3")
    effort.set(Effort.MAX)
    excludeFilter.set(file("config/spotbugs/excludeFilter.xml"))
}

tasks.withType<SpotBugsTask>().configureEach {
    val taskName = name
    val html = reports.maybeCreate("html") as SpotBugsReport
    html.required.set(true)
    html.outputLocation.set(layout.buildDirectory.file("reports/spotbugs/$taskName.html"))
}

tasks {
    named("checkstyleTest") { enabled = false }
    named("pmdTest") { enabled = false }
    named("spotbugsTest") { enabled = false }
}

tasks.check {
    dependsOn(
        tasks.jacocoTestReport,
        tasks.checkstyleMain,
        tasks.pmdMain,
        tasks.spotbugsMain
    )
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "json-assert"
            from(components["java"])
        }
    }
}
