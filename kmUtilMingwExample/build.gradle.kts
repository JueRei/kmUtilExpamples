import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("multiplatform") version "1.4.21"
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

