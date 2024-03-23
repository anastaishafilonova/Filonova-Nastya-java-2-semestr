package book.service.service;

import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookService.class, AuthorService.class})
public class BookServiceTest extends DatabaseSuite {

  @Autowired
  private BookService bookService;
  @Autowired
  private AuthorService authorService;

  @Test
  public void createBookTest() {
    Author author = authorService.createAuthor("Джек", "Лондон");
    BookResponse book = bookService.createBook("Джек", "Лондон", "Мартин Иден");
    assertEquals(book.authorId(), author.getId());
    assertEquals(book.title(), "Мартин Иден");
  }
}

