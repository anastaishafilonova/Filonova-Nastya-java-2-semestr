package book.service.controller;

import book.service.service.BookInfo;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthorRegistryGateway {
  private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

  private final RestTemplate restTemplate;

  @Autowired
  public AuthorRegistryGateway(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @RateLimiter(name = "createBook", fallbackMethod = "fallbackRateLimiter")
  @CircuitBreaker(name = "createBook", fallbackMethod = "fallbackCircuitBreaker")
  @Retry(name = "createBook")
  public boolean createBook(String firstName, String lastName, String title, String requestId) {
    try {
      LOGGER.info("Started book creation");
      HttpHeaders headers = new HttpHeaders();
      // фиксируем уникальный request-id
      headers.add("X-REQUEST-ID", requestId);
      restTemplate.exchange(
          "/api/author-registry/create?firstName={firstName}&lastName={lastName}&title={title}",
          HttpMethod.GET,
          new HttpEntity<>(headers),
          String.class,
          Map.of("firstName", firstName, "lastName", lastName, "title", title)
      );
      LOGGER.info("SuccessfulCreation");
      return true;
    } catch (RestClientException e) {
      LOGGER.warn("Have not managed to create book in author-registry-service");
      throw e;
    }
  }

  public void fallbackRateLimiter(String firstName, String lastName, String title, String requestId, RequestNotPermitted e) {
    LOGGER.warn("Error due to Rate Limiting options", e);
    throw new RestClientException(e.getMessage());
  }

  public void fallbackCircuitBreaker(String firstName, String lastName, String title, String requestId, CallNotPermittedException e) {
    LOGGER.warn("CircuitBreaker was limited");
    LOGGER.warn("Error due to Circuit Breaker options", e);
    throw new RestClientException(e.getMessage());
  }

  @RateLimiter(name = "checkBook", fallbackMethod = "fallbackRateLimiter")
  @CircuitBreaker(name = "checkBook", fallbackMethod = "fallbackCircuitBreaker")
  @Retry(name = "checkBook")
  public boolean checkBook(BookInfo bookInfo, String requestId) {
    String firstName = bookInfo.fistName();
    String lastName = bookInfo.lastName();
    String title = bookInfo.title();
    try {
      HttpHeaders headers = new HttpHeaders();
      // фиксируем уникальный request-id
      headers.add("X-REQUEST-ID", requestId);
      ResponseEntity<AuthorRegistryResponse> response = restTemplate.exchange(
          "/api/author-registry/book?firstName={firstName}&lastName={lastName}&title={title}",
          HttpMethod.GET,
          new HttpEntity(headers),
          AuthorRegistryResponse.class,
          Map.of("firstName", firstName, "lastName", lastName, "title", title)
      );
      LOGGER.info("Received response from author-registry-service {}", response);
      return response.getBody().value();
    } catch (RestClientException e) {
      LOGGER.warn("Have not managed to check this book in author-registry-service");
      throw e;
    }
  }

  public boolean fallbackRateLimiter(BookInfo bookInfo, String requestId, RequestNotPermitted e) {
    LOGGER.warn("Error due to Rate Limiting options", e);
    throw new RestClientException(e.getMessage());
  }

  public boolean fallbackCircuitBreaker(BookInfo bookInfo, String requestId, CallNotPermittedException e) {
    LOGGER.warn("Error due to Circuit Breaker options", e);
    throw new RestClientException(e.getMessage());
  }
}