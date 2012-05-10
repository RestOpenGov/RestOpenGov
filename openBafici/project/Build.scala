import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "openBafici"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "ar.com.restba" % "restba" % "1.0.2"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += ("Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository")
    )

}
