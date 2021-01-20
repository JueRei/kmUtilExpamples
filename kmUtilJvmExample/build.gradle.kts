import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
    java
}

group = "de.rdvsb"
version = "1.0-SNAPSHOT"

val kotlin_version = kotlin.coreLibrariesVersion  // read setting from kotlin plugin

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("de.rdvsb:kmapi-jvm:0.1-SNAPSHOT")
    implementation("de.rdvsb:kmutil-jvm:0.1-SNAPSHOT")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks {
    // some default application args for the gradle run task
    run.get().args = arrayListOf("--help")

}

// provide a manifest for uber(fat) jar
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

// build an uber(fat) jar
tasks.register<Jar>("fatJar") {
    group = "build"
    archiveClassifier.set("fat")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}