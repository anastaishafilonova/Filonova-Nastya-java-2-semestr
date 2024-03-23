package book.service.controller;

import book.service.controller.request.Request;
import book.service.controller.response.ApiError;
import book.service.controller.response.BookResponse;
import book.service.entity.Author;
import book.service.entity.Book;
import book.service.service.AuthorService;
import book.service.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import book.service.repository.exception.BookNotFoundException;


@RestController
@RequestMapping("/api/book")
public class BookController {
  private final BookService bookService;
  private static final Logger logger = LoggerFactory.getLogger(BookController.class);

  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping("")
  public BookResponse createBook(@Valid @RequestBody Request.RequestToCreateBook request) {
    return (bookService.createBook(request.getFirstName(), request.getLastName(), request.getTitle()));
  }

  @PutMapping("/{id}")
  public void updateBook(@PathVariable Long id, @Valid @RequestBody Request.RequestToUpdateBook request) {
    bookService.updateBook(id, request.getTitle());
  }

  @DeleteMapping("/{id}")
  public void deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> BookNotFoundExceptionHandler(BookNotFoundException e) {
    return new ResponseEntity<>(
        new ApiError(e.getMessage()),
        HttpStatus.NOT_FOUND
    );
  }
}
