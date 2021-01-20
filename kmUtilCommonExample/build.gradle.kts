import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("multiplatform") version "1.4.21"
	//application
}

group = "de.rdvsb"
version = "1.0-SNAPSHOT"

val kotlin_version = kotlin.coreLibrariesVersion  // read setting from kotlin plugin

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}


kotlin {
	jvm {
		withJava() // Includes Java sources into the JVM target’s compilations.
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
		testRuns["test"].executionTask.configure {
			useJUnit()
		}

        // add a build -> fatJar to Gradle tasks
		compilations {
			val main = getByName("main")
			tasks {
				register<Jar>("fatJar") {
					group = "build"
					dependsOn(build)
					manifest {
						attributes["Main-Class"] = "MainKt"
					}
					from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) }, main.output.classesDirs)
					//archiveBaseName.set("${project.name}-fat.") // includes version
					archiveFileName.set("${project.name}.jar") // without version ("kmutil-common-example.jar"
				}
			}
		}
	}

	val hostOs = System.getProperty("os.name")
	val isMingwX64 = hostOs.startsWith("Windows")
	val nativeTarget = when {
		hostOs == "Mac OS X" -> macosX64("native")
		hostOs == "Linux" -> linuxX64("native")
		isMingwX64 -> mingwX64("native")
		else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
	}

	nativeTarget.apply {
		binaries {
			executable {
				entryPoint = "main"
			}
		}
	}
	sourceSets {
		val commonMain by getting {
			dependencies {
				api ("de.rdvsb:kmapi:0.1-SNAPSHOT")
				api ("de.rdvsb:kmutil:0.1-SNAPSHOT")
			}
		}

		val nativeMain by getting {
			dependencies {
				api ("de.rdvsb:kmapi:0.1-SNAPSHOT")
				api ("de.rdvsb:kmutil:0.1-SNAPSHOT")
			}
		}
		val nativeTest by getting {
			dependencies {
				api ("de.rdvsb:kmapi:0.1-SNAPSHOT")
				api ("de.rdvsb:kmutil:0.1-SNAPSHOT")
			}
		}
	}
}

// add a run->jarMain to Gradle task
val jarMain by tasks.creating(JavaExec::class) {
	group = "run"
	main = "MainKt"
	kotlin {
		val main = targets["jvm"].compilations["main"]
		dependsOn(main.compileAllTaskName)
		classpath(
			{ main.output.allOutputs.files },
			{ configurations["jvmRuntimeClasspath"] }
		)
	}
	///disable app icon on macOS
	systemProperty("java.awt.headless", "true")
}

//application {
////    mainClass.set ("Main")
//    mainClassName = "MainKt"
//    println ("application mainClass=${mainClass.get()}")
//}