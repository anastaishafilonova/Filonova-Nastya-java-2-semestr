package book.service;

import book.service.controller.AuthorRegistryGateway;
import book.service.controller.AuthorRegistryResponse;
import book.service.controller.RestTemplateConfiguration;
import book.service.controller.request.Request;
import book.service.controller.response.AuthorResponse;
import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import book.service.entity.Book;
import book.service.entity.Tag;
import book.service.repository.AuthorRepository;
import book.service.repository.BookRepository;
import book.service.repository.TagRepository;
import book.service.service.AuthorService;
import book.service.service.BookService;
import book.service.service.DatabaseSuite;
import book.service.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.mockserver.client.server.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationTest extends DatabaseSuite {

  @MockBean
  private AuthorRegistryGateway authorRegistryGateway;
  @Autowired
  private TestRestTemplate rest;

  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private TagRepository tagRepository;
  @Autowired
  private BookService bookService;
  @Autowired
  private TagService tagService;
  @Autowired
  private AuthorService authorService;

  private BookResponse book;
  private Author author;
  private Tag tag;

  @BeforeEach
  public void beforeEach() {
    bookRepository.deleteAll();
    tagRepository.deleteAll();
    authorRepository.deleteAll();
    //author = authorService.createAuthor("Джейн", "Остин");
    //book = bookService.createBook("Джейн", "Остин", "Маленькие женщины");
    //tag = tagService.createTag("Драма", book.id());
  }

  @Test
  void shouldCreateBook() {
    when(authorRegistryGateway.createBook(any(), any(), any(), any())).thenReturn(true);
        ResponseEntity<BookResponse> createBookResponse;
    createBookResponse = rest.postForEntity("/api/book", new Request.RequestToCreateBook("Джейн", "Остин", "Маленькие женщины"), BookResponse.class);
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());
    BookResponse createBookResponseBody = createBookResponse.getBody();
    Assertions.assertEquals("Маленькие женщины", createBookResponseBody.title());
  }


  @Test
  void shouldUpdateAuthor() {
    Author author = authorService.createAuthor("Лев", "Толстой");

    ResponseEntity<AuthorResponse> updateAuthorResponse;
    HttpEntity<Author> newAuthor = new HttpEntity<>(new Author("Алексей", "Толстой"));
    updateAuthorResponse = rest.exchange("/api/author/{id}", HttpMethod.PUT, newAuthor, AuthorResponse.class, Map.of("id", author.getId()));
    assertTrue(updateAuthorResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + updateAuthorResponse.getStatusCode());
    AuthorResponse updateAuthorResponseBody = updateAuthorResponse.getBody();
    Assertions.assertEquals("Алексей", updateAuthorResponseBody.firstName());
  }

  @Test
  void shouldDeleteTag() {
    BookResponse book = bookService.createBook("Михаил", "Булгаков", "Собачье сердце");
    tag = tagService.createTag("Повесть", book.id());
    when(authorRegistryGateway.checkBook(any(), any())).thenReturn(true);
    ResponseEntity<Void> deleteTagResponse;
    deleteTagResponse = rest.exchange("/api/tag/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, Map.of("id", tag.getId()));
    assertTrue(deleteTagResponse.getStatusCode().is2xxSuccessful(), "Unnexpected status code: " + deleteTagResponse.getStatusCode());
  }
}
