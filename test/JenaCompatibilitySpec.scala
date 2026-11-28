import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets.UTF_8

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import org.testcontainers.utility.DockerImageName

@RunWith(classOf[JUnitRunner])
class JenaCompatibilitySpec extends Specification {
  private class RdfEndpoint extends GenericContainer[RdfEndpoint](DockerImageName.parse("busybox:1.37.0"))

  "The configured Jena library" should {
    "load and parse RDF from a mock endpoint on the current JVM" in {
      // Jena 2 and newer releases use different package names.
      // Probe the resource first so JVM linkage errors from the selected Jena propagate.
      val loader = getClass.getClassLoader
      val modelPackage =
        if (loader.getResource("org/apache/jena/rdf/model/ModelFactory.class") != null)
          "org.apache.jena.rdf.model"
        else
          "com.hp.hpl.jena.rdf.model"

      val factory = Class.forName(s"$modelPackage.ModelFactory", true, loader)
      val modelType = Class.forName(s"$modelPackage.Model", true, loader)
      val model = factory.getMethod("createDefaultModel").invoke(null)
      try {
        val rdf = "<urn:estat:item> <urn:estat:label> \"Example\" .\n".getBytes(UTF_8)
        val endpoint = new RdfEndpoint()
          .withCopyToContainer(Transferable.of(rdf), "/data.ttl")
          .withCommand("httpd", "-f", "-p", "8080", "-h", "/")
          .withExposedPorts(8080)
          .waitingFor(Wait.forHttp("/data.ttl"))

        try {
          endpoint.start()
          val url = new URL("http", endpoint.getHost, endpoint.getMappedPort(8080), "/data.ttl")
          val connection = url.openConnection()
          connection.setConnectTimeout(5000)
          connection.setReadTimeout(5000)
          val input = connection.getInputStream
          try {
            modelType.getMethod("read", classOf[InputStream], classOf[String], classOf[String])
              .invoke(model, input, url.toExternalForm, "TURTLE")
          } finally input.close()

          modelType.getMethod("size").invoke(model) must equalTo(java.lang.Long.valueOf(1L))
        } finally endpoint.close()
      } finally modelType.getMethod("close").invoke(model)
    }
  }
}
