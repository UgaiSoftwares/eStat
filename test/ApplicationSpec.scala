import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import play.api.test.Helpers._
import play.api.db.DBModule
import play.api.db.HikariCPModule

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  def application = new GuiceApplicationBuilder()
    .disable(classOf[DBModule], classOf[HikariCPModule]).build()

  "Application" should {
    "send 404 on an unknown path" in {
      val app = application
      running(app) {
        status(route(app, FakeRequest(GET, "/boum")).get) must equalTo(NOT_FOUND)
      }
    }
    "render the index page" in {
      val app = application
      running(app) {
        val home = route(app, FakeRequest(GET, "/")).get
        status(home) must equalTo(OK)
        contentType(home) must beSome("text/html")
        contentAsString(home) must contain("Your new application is ready.")
      }
    }
  }
}
