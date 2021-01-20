import kotlin.time.Duration
import de.rdvsb.kmapi.*
import de.rdvsb.kmutil.*
import kotlin.time.ExperimentalTime
import java.net.URISyntaxException




@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {

	logMessage('I', "kmutil-jvm example start (${System.getProperty("app.name")}/${java.lang.System.getProperty("app.name")} on ${System.getProperty("os.name")})")
	val f = File("/tmp/.x.x")

	try {

		// Get path of the JAR file
		val jarPath: String = args::class.java
			.getProtectionDomain()
			.getCodeSource()
			.getLocation()
			.toURI()
			.getPath()
		println("JAR Path : $jarPath")

		// Get name of the JAR file
		val jarName = jarPath.substring(jarPath.lastIndexOf("/") + 1)
		java.lang.System.out.printf("JAR Name: $jarName")
		logMessage('I', "jarName=$jarName")
	} catch (e: URISyntaxException) {
		e.printStackTrace()
	}
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

	logMessage('I', "kmutil-jvm example end")

}