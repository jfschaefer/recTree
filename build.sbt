name := "recTree"
version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies += "org.scalafx" %% "scalafx" % "2.2.76-R11"

//for Java 7:
unmanagedJars in Compile += Attributed.blank(file(scala.util.Properties.javaHome) / "lib" / "jfxrt.jar")
