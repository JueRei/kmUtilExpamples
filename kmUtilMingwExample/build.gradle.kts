import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("multiplatform")
	//kotlin("jvm") version "1.5.21"
}

group = "de.rdvsb"
version = "1.0-SNAPSHOT"

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
	val nativeTarget = mingwX64("native")

	nativeTarget.apply {
		binaries {
			executable {
				entryPoint = "main"
			}
		}
	}
	sourceSets {
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

