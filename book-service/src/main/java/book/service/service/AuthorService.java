package book.service.service;

import book.service.controller.BookController;
import book.service.entity.Author;
import book.service.entity.Book;
import book.service.repository.AuthorRepository;
import book.service.repository.BookRepository;
import book.service.repository.exception.BookNotFoundException;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AuthorService {
  private AuthorRepository authorRepository;
  private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);


  @Autowired
  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  protected AuthorService() {
  }

  @Transactional
  public Author createAuthor(String firstName, String lastName) {
    if (authorRepository.findByFirstNameAndLastName(firstName, lastName) != null) {
      return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    } else {
      Author author = new Author(firstName, lastName);
      return authorRepository.save(author);
    }
  }

  @Transactional
  public void updateAuthor(Long id, String firstName, String lastName) {
    Author author = authorRepository.findById(id).orElseThrow();
    author.setFirstName(firstName);
    author.setLastName(lastName);
    authorRepository.save(author);
  }

  @Transactional
  public void deleteAuthor(Long id) {
    Author author = authorRepository.findById(id).orElseThrow();
    authorRepository.delete(author);
  }

  @Transactional
  public void addNewBook(Long authorId, String title) {
    Author author = authorRepository.findById(authorId).orElseThrow();
    var bookList = author.getBookList();
    author.addBookInBookList(title);
    authorRepository.save(author);
  }

  @Transactional
  public void deleteBook(Long authorId, Long bookId) {
    Author author = authorRepository.findById(authorId).orElseThrow();
    author.deleteBookFromBookList(bookId);
    authorRepository.save(author);
  }
}
