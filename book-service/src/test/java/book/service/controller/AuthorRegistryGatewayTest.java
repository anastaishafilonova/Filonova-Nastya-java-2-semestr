package book.service.controller;

import java.util.*;
import book.service.service.BookInfo;
import org.junit.jupiter.api.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClientException;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.Assert.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
@SpringBootTest(classes = {AuthorRegistryGateway.class, RestTemplateConfiguration.class},
properties = {"author-registry.service.timeout.seconds=1"})
class AuthorRegistryGatewayTest {
  @Container
  public static final MockServerContainer mockServer =
      new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.13.2"));

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("author-registry.service.base.url", mockServer::getEndpoint);
  }

  @Autowired
  private AuthorRegistryGateway authorRegistryGateway;

  @Test
  void shouldCheckBook() {
    var client = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
    client
        .when(request()
            .withMethod("GET")
            .withPath("/api/author-registry/book")
            .withHeader("X-REQUEST-ID")
            .withQueryStringParameters(Map.of("firstName", List.of("Michail"), "lastName", List.of("Bulgakov"), "title", List.of("Master and Margarita"))))
        .respond(response(
                """
                    {"value": true}"""
            ).withHeader("Content-Type", "application/json")
        );
    Boolean check = authorRegistryGateway.checkBook(new BookInfo("Michail", "Bulgakov", "Master and Margarita"), "dfs");

    assertTrue(check);
  }
  @Test
  void shouldCreateBook() {
    var client = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
    client
        .when(request()
            .withMethod("GET")
            .withPath("/api/author-registry/create")
            .withHeader("X-REQUEST-ID", "1")
            .withQueryStringParameters(Map.of("firstName", List.of("Michail"), "lastName", List.of("Bulgakov"), "title", List.of("Master and Margarita"))))
        .respond(response()
            .withDelay(Delay.seconds(10))
        );

    assertThrows(
        RestClientException.class,
        () -> authorRegistryGateway.createBook("Michail", "Bulgakov", "Master and Margarita", "1")
    );
  }
}