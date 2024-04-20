package author.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AuthorRegistryControllerTest {
  @Autowired
  private TestRestTemplate rest;

  @Test
  void shouldCreateBook() {
    ResponseEntity<String> createBookResponse;
    HttpHeaders headers = new HttpHeaders();
    // фиксируем уникальный request-id
    headers.add("X-REQUEST-ID", "first");
    createBookResponse = rest.exchange(
        "/api/author-registry/create?firstName={firstName}&lastName={lastName}&title={title}",
        HttpMethod.GET,
        new HttpEntity<>(headers),
        String.class,
        Map.of("firstName", "Mark", "lastName", "Twen", "title", "Tom Soyer")
    );
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());
    String createBookResponseBody = createBookResponse.getBody();
    assertEquals("This book was added", createBookResponseBody);
  }

  @Test
  void shouldCheckBookFalse() {
    ResponseEntity<AuthorRegistryResponse> checkBookResponse;
    HttpHeaders headers = new HttpHeaders();
    // фиксируем уникальный request-id
    headers.add("X-REQUEST-ID", "second");
    checkBookResponse = rest.exchange(
        "/api/author-registry/book?firstName={firstName}&lastName={lastName}&title={title}",
        HttpMethod.GET,
        new HttpEntity(headers),
        AuthorRegistryResponse.class,
        Map.of("firstName", "Aleksandr", "lastName", "Pushkin", "title", "Pikovaya Dama")
    );
    assertTrue(checkBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + checkBookResponse.getStatusCode());
    AuthorRegistryResponse getBookResponseBody = checkBookResponse.getBody();
    assertFalse(getBookResponseBody.value());
  }

  @Test
  void shouldCheckBookTrue() {
    ResponseEntity<AuthorRegistryResponse> checkBookResponse;
    HttpHeaders headers = new HttpHeaders();
    // фиксируем уникальный request-id
    headers.add("X-REQUEST-ID", "third");
    rest.exchange(
        "/api/author-registry/create?firstName={firstName}&lastName={lastName}&title={title}",
        HttpMethod.GET,
        new HttpEntity<>(headers),
        String.class,
        Map.of("firstName", "Aleksandr", "lastName", "Pushkin", "title", "Pikovaya Dama")
    );
    checkBookResponse = rest.exchange(
        "/api/author-registry/book?firstName={firstName}&lastName={lastName}&title={title}",
        HttpMethod.GET,
        new HttpEntity(headers),
        AuthorRegistryResponse.class,
        Map.of("firstName", "Aleksandr", "lastName", "Pushkin", "title", "Pikovaya Dama")
    );
    assertTrue(checkBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + checkBookResponse.getStatusCode());
    AuthorRegistryResponse getBookResponseBody = checkBookResponse.getBody();
    assertTrue(getBookResponseBody.value());
  }
}