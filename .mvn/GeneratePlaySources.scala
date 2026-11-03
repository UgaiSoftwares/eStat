import java.io.File
import java.nio.file.Files
import scala.jdk.CollectionConverters._
import play.routes.compiler.{InjectedRoutesGenerator, RoutesCompiler}
import play.twirl.compiler.TwirlCompiler

val base = project.getBasedir
val generated = new File(project.getBuild.getDirectory, "generated-sources/play")
// Recreate outputs so deleted routes and templates cannot leave stale sources.
if (generated.exists()) {
  val paths = Files.walk(generated.toPath)
  try paths.iterator.asScala.toSeq.reverse.foreach(Files.delete(_))
  finally paths.close()
}
generated.mkdirs()
val app = new File(base, "app")
val templates = Files.walk(app.toPath)
try templates.iterator.asScala.filter(_.toString.endsWith(".scala.html")).foreach { path =>
  TwirlCompiler.compile(path.toFile, app, generated, "play.twirl.api.HtmlFormat",
    TwirlCompiler.defaultImports("2.13.16") ++ Seq("_root_.play.twirl.api._", "_root_.play.api.templates.PlayMagic._", "_root_.controllers._"))
} finally templates.close()
val task = RoutesCompiler.RoutesCompilerTask(new File(base, "conf/routes"),
  Seq("play.libs.F"), true, true, true, false)
RoutesCompiler.compile(task, InjectedRoutesGenerator, generated) match {
  case Left(errors) => sys.error(errors.mkString("\n"))
  case Right(_) => ()
}

// sbt-web exposes versioned WebJar entries under public/lib without the version.
project.getArtifacts.asScala.filter(_.getGroupId == "org.webjars").foreach { artifact =>
  val jar = new java.util.jar.JarFile(artifact.getFile)
  try jar.entries.asScala.filterNot(_.isDirectory).foreach { entry =>
    val prefix = "META-INF/resources/webjars/"
    if (entry.getName.startsWith(prefix)) {
      val parts = entry.getName.stripPrefix(prefix).split("/", 3)
      if (parts.length == 3) {
        val destination = new File(project.getBuild.getOutputDirectory,
          s"public/lib/${parts(0)}/${parts(2)}").toPath
        Files.createDirectories(destination.getParent)
        val input = jar.getInputStream(entry)
        try Files.copy(input, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        finally input.close()
      }
    }
  } finally jar.close()
}
