package masera.deviajeusersandauth.configs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringDocConfigTest {

  @LocalServerPort
  private int port;

  @Test
  void getDocumentation() throws IOException {

    WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();

    String responseBody = webTestClient.get()
            .uri("/v3/api-docs")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(responseBody, "El cuerpo de la respuesta no debe ser nulo");
    assertTrue(responseBody.contains("openapi"),
            "La respuesta debe contener la especificación OpenAPI");

    Path specs = Paths.get("docs/api_doc");
    Files.createDirectories(specs);
    Files.writeString(specs.resolve("swagger.json"), responseBody);
  }
}