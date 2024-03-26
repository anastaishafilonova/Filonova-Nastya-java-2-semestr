package book.service.service;

import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import book.service.entity.Book;
import book.service.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookService.class, AuthorService.class})
public class BookServiceTest extends DatabaseSuite {

  @Autowired
  private BookService bookService;
  @Autowired
  private AuthorService authorService;

  @Autowired
  private BookRepository bookRepository;

  @Test
  public void createBookTest() {
    Author author = authorService.createAuthor("Джек", "Лондон");
    BookResponse book = bookService.createBook("Джек", "Лондон", "Мартин Иден");
    assertEquals(book.authorId(), author.getId());
    assertEquals(book.title(), "Мартин Иден");
  }

  @Test
  public void deleteBookTest() {
    BookResponse book = bookService.createBook("Джейн", "Остин", "Гордость и предубеждение");
    bookService.deleteBook(book.id());
    assertEquals(bookRepository.findById(book.id()), Optional.empty());
  }

  @Test
  public void updateBookTest() {
    BookResponse book = bookService.createBook("Агата", "Кристи", "Убийство в восточном");
    bookService.updateBook(book.id(), "Убийство в восточном экспрессе");
    assertEquals(bookRepository.findByTitle("Убийство в восточном экспрессе").getTitle(), "Убийство в восточном экспрессе");
  }
}

