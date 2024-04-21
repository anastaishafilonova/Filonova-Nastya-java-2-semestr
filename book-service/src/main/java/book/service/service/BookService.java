package book.service.service;

import book.service.controller.RatingResult;
import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import book.service.entity.Book;
import book.service.repository.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private BookRepository bookRepository;
  private AuthorService authorService;
  private static final Logger logger = LoggerFactory.getLogger(BookService.class);
  private final ObjectMapper objectMapper = new ObjectMapper();


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

  @Transactional
  public BookInfo getBookInfo(Long id) {
    Book book = bookRepository.findById(id).orElseThrow();
    Author author = book.getAuthor();
    return new BookInfo(author.getFirstName(), author.getLastName(), book.getTitle());
  }

  @Transactional
  public void process(String message) throws JsonProcessingException {
    var result = objectMapper.readValue(message, RatingResult.class);
    Book book = bookRepository.findById(result.bookId()).orElseThrow();
    book.setRating(result.rating());
    bookRepository.save(book);
  }
}
