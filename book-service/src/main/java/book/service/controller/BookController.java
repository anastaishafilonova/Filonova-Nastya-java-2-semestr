package book.service.controller;

import book.service.controller.request.Request;
import book.service.controller.response.ApiError;
import book.service.entity.Book;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import book.service.repository.BookRepository;
import book.service.repository.exception.BookNotFoundException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class BookController {
  private final BookRepository bookRepository;

  @Autowired
  public BookController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @PostMapping("/book")
  public Book createBook(@Valid @RequestBody Request.RequestToCreateBook request) {
    return bookRepository.createBook(request.getAuthor(), request.getTitle(), request.getTags());
  }

  @PutMapping("/book/{id}")
  public Book updateBook(@PathVariable int id, @Valid @RequestBody Request.RequestToUpdateBook request) {
    return bookRepository.updateBook(id, request.getAuthor(), request.getTitle());
  }

  @DeleteMapping("/book/{id}")
  public void deleteBook(@PathVariable int id) {
    bookRepository.deleteBookById(id);
  }

  @GetMapping("/books/{tag}")
  public Book getBookWithTag(@PathVariable String tag){
    return bookRepository.getBookWithTag(tag);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> BookNotFoundExceptionHandler(BookNotFoundException e) {
    return new ResponseEntity<>(
        new ApiError(e.getMessage()),
        HttpStatus.NOT_FOUND
    );
  }
}
