package book.service.controller;

import book.service.service.BookInfo;
import io.github.resilience4j.springboot3.ratelimiter.autoconfigure.RateLimiterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(
    classes = {
        AuthorRegistryGateway.class
    },
    properties = {
        "resilience4j.ratelimiter.instances.checkBook.limitForPeriod=1",
        "resilience4j.ratelimiter.instances.checkBook.limitRefreshPeriod=1h",
        "resilience4j.ratelimiter.instances.checkBook.timeoutDuration=0"
    }
)
@Import(RateLimiterAutoConfiguration.class)
@EnableAspectJAutoProxy
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RateLimiterAuthorRegistryTest {
  @Autowired
  private AuthorRegistryGateway authorRegistryGateway;
  @MockBean
  private RestTemplate restTemplate;

  @Test
  void shouldRejectRequestAfterFirstServerSlowResponse() {
    HttpHeaders headers = new HttpHeaders();
    // фиксируем уникальный request-id
    headers.add("X-REQUEST-ID", "bfs");
    when(restTemplate.exchange(
        "/api/author-registry/book?firstName={firstName}&lastName={lastName}&title={title}",
        HttpMethod.GET,
        new HttpEntity(headers),
        AuthorRegistryResponse.class,
        Map.of("firstName", "Lev", "lastName", "Tolstoy", "title", "War and peace")
    )).thenAnswer((Answer<ResponseEntity<AuthorRegistryResponse>>) invocation -> {
      Thread.sleep(2000);
      return new ResponseEntity<>(new AuthorRegistryResponse(true), HttpStatus.OK);
    });

    assertDoesNotThrow(
        () -> authorRegistryGateway.checkBook(new BookInfo("Lev", "Tolstoy", "War and peace"), "bfs")
    );

    assertThrows(
        RestClientException.class,
        () -> authorRegistryGateway.checkBook(new BookInfo("Lev", "Tolstoy", "War and peace"), "bfs")
    );
  }
}