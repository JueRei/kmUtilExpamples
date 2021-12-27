import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    java
}

group = "de.rdvsb"
version = "1.1-SNAPSHOT"

//val kotlin_version = kotlin.coreLibrariesVersion  // read setting from kotlin plugin
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotlin_coroutines_version: String by project
val kotlin_serialization_version: String by project
val kotlin_date_version: String by project
val kmapi_version: String by project
val kmutil_version: String by project

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version")
    implementation("de.rdvsb:kmapi-jvm:$kmapi_version")
    implementation("de.rdvsb:kmutil-jvm:$kmutil_version")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnit()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
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
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}