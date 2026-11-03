import java.net.{HttpURLConnection, URL}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.db.{DBModule, HikariCPModule}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.TestServer
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {
  "Application" should {
    "serve the index and static assets over HTTP" in {
      val app = new GuiceApplicationBuilder().disable(classOf[DBModule], classOf[HikariCPModule]).build()
      val server = TestServer(0, app)
      running(server) {
        def get(path: String): String = {
          val connection = new URL(s"http://localhost:${server.runningHttpPort.get}$path")
            .openConnection().asInstanceOf[HttpURLConnection]
          connection.setConnectTimeout(5000)
          connection.setReadTimeout(5000)
          try {
            val source = scala.io.Source.fromInputStream(connection.getInputStream, "UTF-8")
            try source.mkString finally source.close()
          } finally connection.disconnect()
        }
        get("/") must contain("Your new application is ready.")
        get("/assets/lib/bootstrap/css/bootstrap.min.css") must contain("Bootstrap")
        get("/assets/lib/jquery/jquery.min.js") must contain("jQuery")
        get("/assets/javascripts/hello.js") must contain("Welcome to your Play")
      }
    }
  }
}
