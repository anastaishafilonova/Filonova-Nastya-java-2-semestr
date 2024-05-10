package book.service;

import book.service.controller.AuthorRegistryGateway;
import book.service.controller.request.Request;
import book.service.controller.response.AuthorResponse;
import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import book.service.entity.Role;
import book.service.entity.Tag;
import book.service.repository.AuthorRepository;
import book.service.repository.BookRepository;
import book.service.repository.TagRepository;
import book.service.service.AuthorService;
import book.service.service.BookService;
import book.service.service.DatabaseSuite;
import book.service.service.TagService;
import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

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
    var headers = new HttpHeaders();
    String base64AuthCredentials =
        new String(Base64.encodeBase64("TEST:TEST".getBytes()));
    headers.add("Authorization", "Basic " + base64AuthCredentials);
    when(authorRegistryGateway.createBook(any(), any(), any(), any())).thenReturn(true);
    ResponseEntity<BookResponse> createBookResponse;
    createBookResponse = rest.exchange("/api/book", HttpMethod.POST, new HttpEntity<>(new Request.RequestToCreateBook("Джейн", "Остин", "Маленькие женщины"), headers), BookResponse.class);
    assertTrue(createBookResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + createBookResponse.getStatusCode());
    BookResponse createBookResponseBody = createBookResponse.getBody();
    Assertions.assertEquals("Маленькие женщины", createBookResponseBody.title());
  }


  @Test
  void shouldUpdateAuthor() {
    Author author = authorService.createAuthor("Лев", "Толстой");
    var headers = new HttpHeaders();
    String base64AuthCredentials =
        new String(Base64.encodeBase64("TEST:TEST".getBytes()));
    headers.add("Authorization", "Basic " + base64AuthCredentials);
    ResponseEntity<AuthorResponse> updateAuthorResponse;
    updateAuthorResponse = rest.exchange("/api/author/{id}", HttpMethod.PUT, new HttpEntity<>(new Author("Алексей", "Толстой"), headers), AuthorResponse.class, Map.of("id", author.getId()));
    assertTrue(updateAuthorResponse.getStatusCode().is2xxSuccessful(), "Unexpected status code: " + updateAuthorResponse.getStatusCode());
    AuthorResponse updateAuthorResponseBody = updateAuthorResponse.getBody();
    Assertions.assertEquals("Алексей", updateAuthorResponseBody.firstName());
  }

  @Test
  void shouldDeleteTag() {
    var headers = new HttpHeaders();
    String base64AuthCredentials =
        new String(Base64.encodeBase64("TEST:TEST".getBytes()));
    headers.add("Authorization", "Basic " + base64AuthCredentials);
    BookResponse book = bookService.createBook("Михаил", "Булгаков", "Собачье сердце");
    tag = tagService.createTag("Повесть", book.id());
    when(authorRegistryGateway.checkBook(any(), any())).thenReturn(true);
    ResponseEntity<Void> deleteTagResponse;
    deleteTagResponse = rest.exchange("/api/tag/{id}", HttpMethod.DELETE, new HttpEntity<>(HttpEntity.EMPTY, headers), Void.class, Map.of("id", tag.getId()));
    assertTrue(deleteTagResponse.getStatusCode().is2xxSuccessful(), "Unnexpected status code: " + deleteTagResponse.getStatusCode());
  }
}
