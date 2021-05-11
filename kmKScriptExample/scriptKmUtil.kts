#!/usr/bin/env kscript

// a simple KTS (no main)

@file:MavenRepository("central", "https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

//@file:MavenRepository("local", "file:///<location of km libs>/repository/")
// Default: load from maven local ($HOME/.m2)
@file:DependsOn("de.rdvsb:kmapi-jvm:0.1.3-SNAPSHOT")
@file:DependsOn("de.rdvsb:kmutil-jvm:0.1.3-SNAPSHOT")

import de.rdvsb.kmapi.*
import de.rdvsb.kmutil.*

fun usage(errText: String? = null): Nothing {
	errText?.let { System.err.println("+++ $errText") }

	System.err.println(
		"""
		|usage1 $appName [--debug] [--quiet|--verbose] [--dir <name>] name*
		|  do something with names
		|  --dir: optional dir
		|  -- stops argument parsing and adds remaining tokens as names
	""".trimMargin()
	)

	exit(9)
}

val MINNAMES = 0
val MAXNAMES = 300
var appName = System.getProperty("app.name")
var isSimulate = false
var debugLevel = 0
var dir = ""
val names = mutableListOf<String>()

args.asList().iterator().apply args@{
	for (arg in this) {
		when (arg) {
			// handle long args first
			"--help"     -> usage()
			"--simulate" -> isSimulate = true
			"--debug"    -> debugLevel++
			"--dir"      -> dir = if (hasNext()) next() else usage("$arg needs a parameter")
			"-", ""      -> names += arg
			"--"         -> names.addAll(asSequence()) // append remaining elements
			else         -> {
				if (arg.isNotEmpty() && arg[0] == '-') {
					// handle short args here
					arg.forEachIndexed { i, ch ->
						i == 0 && return@forEachIndexed
						i == 1 && ch == '-' && usage("unknown long argument \"$arg\"")
						when (ch) {
							'h', 'u' -> usage()
							'd'      -> ++debugLevel
							's'      -> isSimulate = true
							else     -> usage("unknown short argument char \"$ch\" in argument token \"$arg\"")
						}
					}
				} else {
					names.add(arg)
				}
			}
		}
	}
}

if (names.size < MINNAMES) usage("no names supplied")
if (names.size > MAXNAMES) usage("too many arguments")

//logMessage.logName = "${System.getProperty("app.name")}.log"

logMessage('I', "Start $appName}")
logMessage('I', "dir=$dir, names=${names.map { "\"$it\"" }}")
logMessage('I', "Log to ${logMessage.logPath} logDir=${logMessage.logDir}")

logMessage('I', "End log")
