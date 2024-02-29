package book.service.controller;


import book.service.controller.request.Request;
import book.service.entity.Book;
import book.service.repository.exception.BookNotFoundException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BookControllerTest {
  @Autowired
  private TestRestTemplate rest;

  @Test
  void shouldCreateBook() {
    ResponseEntity<Book> createBookResponse;
    createBookResponse = rest.postForEntity("/api/book", new Request.RequestToCreateBook("Mark Twen", "Tom Soyer", Set.of("")), Book.class);
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());
    Book createBookResponseBody = createBookResponse.getBody();
    assertEquals("Mark Twen", createBookResponseBody.getAuthor());
    assertEquals("Tom Soyer", createBookResponseBody.getTitle());
  }

  @Test
  void shouldGetBookByTag() {
    ResponseEntity<Book> createBookResponse;
    createBookResponse = rest.postForEntity("/api/book", new Request.RequestToCreateBook("Mark Twen", "Tom Soyer", Set.of("child")), Book.class);
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());

    ResponseEntity<Book> getBookResponse =
        rest.getForEntity("/api/books/{tag}", Book.class, Map.of("tag", "child"));
    assertTrue(getBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + getBookResponse.getStatusCode());

    Book getBookResponseBody = getBookResponse.getBody();
    assertEquals("Mark Twen", getBookResponseBody.getAuthor());
    assertEquals("Tom Soyer", getBookResponseBody.getTitle());
  }

  @Test
  void shouldUpdateBook() {
    ResponseEntity<Book> createBookResponse;
    createBookResponse = rest.postForEntity("/api/book", new Request.RequestToCreateBook("Mark Twen", "Tom Soyer", Set.of("")), Book.class);
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());

    ResponseEntity<Book> updateBookResponse;
    HttpEntity<Book> book = new HttpEntity<>(new Book("Mark Twen", "T", Set.of("child")));
    updateBookResponse = rest.exchange("/api/book/{id}", HttpMethod.PUT, book, Book.class, Map.of("id", 1));
    assertTrue(updateBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + updateBookResponse.getStatusCode());
    Book updateBookResponseBody = updateBookResponse.getBody();
    assertEquals("Mark Twen", updateBookResponseBody.getAuthor());
    assertEquals("T", updateBookResponseBody.getTitle());
  }

  @Test
  void shouldDeleteBook() {
    ResponseEntity<Book> createBookResponse;
    createBookResponse = rest.postForEntity("/api/book", new Request.RequestToCreateBook("Mark Twen", "Tom Soyer", Set.of("")), Book.class);
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());

    ResponseEntity<Void> deleteBookResponse;
    deleteBookResponse = rest.exchange("/api/book/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, Map.of("id", 1));
    assertTrue(deleteBookResponse.getStatusCode().is2xxSuccessful(), "Unnexpected status code: " + deleteBookResponse.getStatusCode());
  }

  @Test
  void validationTest() {
    ResponseEntity<Book> createBookResponse;
    createBookResponse = rest.postForEntity("/api/book", new Request.RequestToCreateBook(null, "Tom Soyer", Set.of("")), Book.class);
    assertTrue(createBookResponse.getStatusCode().is4xxClientError(), "Unexpected status code: " + createBookResponse.getStatusCode());
  }

  @Test
  void shouldExceptionWhenBookNotFound() {
    ResponseEntity<Void> deleteBookResponse;
    deleteBookResponse = rest.exchange("/api/book/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, Map.of("id", 100));
    assertTrue(deleteBookResponse.getStatusCode().is4xxClientError(), "Unnexpected status code: " + deleteBookResponse.getStatusCode());
  }
}