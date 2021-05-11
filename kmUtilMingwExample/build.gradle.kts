import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("multiplatform") version "1.5.0"
}

group = "de.rdvsb"
version = "1.1-SNAPSHOT"

val kotlin_version = kotlin.coreLibrariesVersion  // read setting from kotlin plugin
val coroutinesVersion = "1.4.3"

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
				api ("de.rdvsb:kmapi:0.+")
				api ("de.rdvsb:kmutil:0.+")
			}
		}

		val nativeTest by getting {
			dependencies {
				api ("de.rdvsb:kmapi:0.+")
				api ("de.rdvsb:kmutil:0.+")
			}
		}
	}
}

