name := "dynaro"
 
version := "1.0"
      
lazy val `dynaro` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( javaJdbc , cache , javaWs )

libraryDependencies ++= {
  val akkaVersion = "2.5.15"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion
  )
}

libraryDependencies ++= {
  Seq(
    "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % "0.18.0",
    "com.lightbend.akka.discovery" %% "akka-discovery-config" % "0.18.0"
  )
}

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.16"

libraryDependencies += "org.reflections" % "reflections" % "0.9.11"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
      