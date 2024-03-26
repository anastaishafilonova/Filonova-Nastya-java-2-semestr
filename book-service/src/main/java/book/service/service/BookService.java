package book.service.service;

import book.service.controller.BookController;
import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import book.service.entity.Book;
import book.service.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Component
public class BookService {
  private BookRepository bookRepository;
  private AuthorService authorService;
  private static final Logger logger = LoggerFactory.getLogger(BookService.class);


  @Autowired
  public BookService(BookRepository bookRepository, AuthorService authorService) {
    this.bookRepository = bookRepository;
    this.authorService = authorService;
  }

  protected BookService() {
  }

  @Transactional
  public BookResponse createBook(String firstName, String lastName, String title) {
    Author author = authorService.createAuthor(firstName, lastName);
    authorService.addNewBook(author.getId(), title);
    Book book = bookRepository.findByTitle(title);
    return new BookResponse(book.getId(), author.getId(), title);
  }

  @Transactional
  public void updateBook(Long id, String title) {
    Book book = bookRepository.findById(id).orElseThrow();
    book.setTitle(title);
    bookRepository.save(book);
  }

  @Transactional
  public void deleteBook(Long id) {
    Book book = bookRepository.findById(id).orElseThrow();
    authorService.deleteBook(book.getAuthor().getId(), id);
  }
}
