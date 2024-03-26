package book.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationTest extends DatabaseSuite {
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

    ResponseEntity<Void> deleteTagResponse;
    deleteTagResponse = rest.exchange("/api/tag/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, Map.of("id", tag.getId()));
    assertTrue(deleteTagResponse.getStatusCode().is2xxSuccessful(), "Unnexpected status code: " + deleteTagResponse.getStatusCode());
  }
}
