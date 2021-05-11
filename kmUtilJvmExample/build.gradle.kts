import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    application
    java
}

group = "de.rdvsb"
version = "1.1-SNAPSHOT"

val kotlin_version = kotlin.coreLibrariesVersion  // read setting from kotlin plugin
val coroutinesVersion = "1.4.3"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("de.rdvsb:kmapi-jvm:0.+")
    implementation("de.rdvsb:kmutil-jvm:0.+")
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
    run.get().args = arrayListOf("--debug")

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

    // // create fat jar with a custom name in a custom destination dir
    // archiveFileName.set("kmexample.jar")
    // archiveBaseName.set("")
    // destinationDirectory.set (File("..")) // e.g. parent of project dir

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    // // copy fat jar from buil dir to destination/path
    // doLast {
	//     println("++doLast from ${outputs.files.asPath}")
	//     copy {
	//         from(outputs.files.asPath)
	//         into ("../kmexample.jar") // e.g. parent of project dir
	//     }
	//
	// }
}