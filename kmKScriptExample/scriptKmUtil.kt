#!/usr/bin/env kscript

@file:CompilerOpts("-jvm-target 1.8")
@file:MavenRepository("local", "file:///files.jet.rdvsb.de/install/Lib/MavenLinux/repository/")
@file:DependsOn("de.rdvsb:kmapi-jvm:0.1.3-SNAPSHOT")
@file:DependsOn("de.rdvsb:kmutil-jvm:0.1.3-SNAPSHOT")

@file:MavenRepository("central", "https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

//import DependsOn
import sun.misc.Signal
import kotlin.contracts.ExperimentalContracts

import de.rdvsb.kmapi.*
import de.rdvsb.kmutil.*
import kotlin.time.ExperimentalTime

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * parse arguments and store the argument values
 *
 * extend BasicGetArgs class from kmtUtil to build real getArgs object
 */
class GetArgs : BasicGetArgs() {
	init {
		appPath = updateAppPath(this)
		basicGetArgs = this // overwrite basicGetArgs initialized with empty placeholder in scriptUtil.kt with the real one
	}

	val MINNAMES = 0
	val MAXNAMES = 300
	var isSimulate = false
	val isNotSimulate get() = !isSimulate

	var isTest = false
	val isNotTest get() = !isTest
	var dir = ""
	val names = mutableListOf<String>()

	/**
	 * parse an [Array] of [String] as commandline parameters and set properties accordingly.
	 *
	 * on errors call [usage] () and exit with rc 9
	 */
	operator fun invoke(args: Array<String>) {
		if (args.size < MINNAMES) usage()
		args.asList().iterator().apply args@{
			for (arg in this) {
				when (arg) {
					// handle long args first
					"--help"     -> usage()
					"--simulate" -> isSimulate = true
					"--verbose"  -> isVerbose = true
					"--quiet"    -> isQuiet = true
					"--test"     -> isTest = true
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
									'q'      -> isQuiet = true
									'v'      -> isVerbose = true
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
		logMessage.isQuiet = isQuiet
	}

	fun usage(errText: String? = null): Nothing {
		errText?.let { System.err.println("+++ $errText") }

		System.err.println(
			"""
			|usage1 $appName [--debug] [--quiet|--verbose] [--dir <name>] [--] name*
			|  do something with names
			|  --dir: optional dir
			|  -- stops argument parsing and adds remaining tokens as names
		""".trimMargin()
		)

		exit(9)
	}
}

var getArgs = GetArgs()

fun main(args: Array<String>) {
	getArgs(args)
	//logMessage.logName = "${System.getProperty("app.name")}.log"

	logMessage('I', "Start ${System.getProperty("app.name")}")
	logMessage('I', "dir=${getArgs.dir}, names=${getArgs.names.map { "\"$it\"" }}")
	logMessage('I', "Log to ${logMessage.logPath} logDir=${logMessage.logDir}")

	logMessage('I', "End log")
}
