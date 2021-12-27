plugins {
	kotlin("multiplatform")
	//application
	//kotlin("jvm") version "1.5.21"
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
				api ("de.rdvsb:kmapi:$kmapi_version")
				api ("de.rdvsb:kmutil:$kmutil_version")
			}
		}

		val nativeMain by getting {
			dependencies {
				api ("de.rdvsb:kmapi:$kmapi_version")
				api ("de.rdvsb:kmutil:$kmutil_version")
			}
		}
		val nativeTest by getting {
			dependencies {
				api ("de.rdvsb:kmapi:$kmapi_version")
				api ("de.rdvsb:kmutil:$kmutil_version")
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
//dependencies {
//	implementation(kotlin("stdlib-jdk8"))
//}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//	jvmTarget = "1.8"
//}
//val compileTestKotlin: KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//	jvmTarget = "1.8"
//}
