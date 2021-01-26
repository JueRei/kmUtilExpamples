import de.rdvsb.kmapi.*
import de.rdvsb.kmutil.*

/**
 * parse arguments and store the argument values
 *
 * extend BasicGetArgs class from scriptUtil.kt to build real getArgs object
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

	logMessage('I', "kmutil-mingw example start (${System.getProperty("app.name")} on ${System.getProperty("os.name")})")
	logMessage('I', "dir=${getArgs.dir}, names=${getArgs.names.map { "\"$it\"" }}, isDebug=${getArgs.isDebug}")

	val f = File("/tmp/.x.x")

	//throw (IOException("Test"))

	println("f=$f")

//	logMessage.logPath = "/tmp/kmFileTest.log"
//	logMessage('I', "test start\nlogging to ${logMessage.logPath}")
//
//	println("name=${f.name} canonicalPath=${f.canonicalPath}")
////	f.outputStream().bufferedWriter().use {
////		it.write("hello")
////	}
//	println("exists=${f.exists}, length=${f.length}, isHidden=${f.isHidden}, canWrite=${f.canWrite}, isSymbolicLink=${f.isSymbolicLink}, isDevice=${f.isDevice}")
//
//
//	println("renameTo=${f.renameTo(KmFile("/tmp/x.y"))}, newPath=${f.path}")

	logMessage('I', "kmutil-mingw example end")

}