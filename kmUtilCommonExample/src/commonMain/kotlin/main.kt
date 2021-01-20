import de.rdvsb.kmapi.*
import de.rdvsb.kmutil.*
import de.rdvsb.kmutil.logMessage
import de.rdvsb.kmutil.logPath
import kotlin.time.Duration

//import de.rdvsb.kmutil.KmLog


fun main(args: Array<String>) {

	System.err.println("asd")
	val processBuilder = ProcessBuilder("ls")
	processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)

	val process = processBuilder.start()
	val inp: InputStream = process.inputStream

	val timeout: Duration = Duration.INFINITE

	val l = inp.bufferedReader().readLines()
	println(l)
	if (timeout.isFinite()) {
		process.waitFor(timeout.toLongMilliseconds(), TimeUnit.MILLISECONDS)
	} else {
		process.waitFor()
	}



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


}