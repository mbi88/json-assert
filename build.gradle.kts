import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("ru.vyarus.quality").version("4.7.0")
    id("java-library")
    id("jacoco")
}

val suitesDir = "src/test/resources/suites/"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.json", "json", "20201115")
    implementation("org.skyscreamer", "jsonassert", "1.5.0")
    implementation("org.testng", "testng", "7.3.0")
    implementation("io.rest-assured", "rest-assured", "4.4.0")
    implementation("com.github.wnameless", "json-flattener", "0.7.1")
}
tasks.test {
    useTestNG {
        // Add test suites
        File(projectDir.absolutePath + "/" + suitesDir)
            .walk()
            .forEach {
                if (it.isFile) {
                    suites(it)
                }
            }

        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("${buildDir}/reports/coverage").get().asFile)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

quality {
    checkstyle = true
    pmd = true
    codenarc = true
    spotbugs = true
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}
