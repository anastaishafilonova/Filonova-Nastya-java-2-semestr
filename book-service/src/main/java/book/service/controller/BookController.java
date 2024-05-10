package book.service.controller;

import book.service.controller.request.Request;
import book.service.controller.response.BookResponse;
import book.service.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;


@RestController
@RequestMapping("/api/book")
public class BookController {
  private final BookService bookService;
  private final AuthorRegistryGateway authorRegistryGateway;
  private static final Logger logger = LoggerFactory.getLogger(BookController.class);

  @Autowired
  public BookController(BookService bookService, AuthorRegistryGateway authorRegistryGateway) {
    this.bookService = bookService;
    this.authorRegistryGateway = authorRegistryGateway;
  }

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("")
  public BookResponse createBook(@Valid @RequestBody Request.RequestToCreateBook request) {
    String requestId = UUID.randomUUID().toString();
    authorRegistryGateway.createBook(request.getFirstName(), request.getLastName(), request.getTitle(), requestId);
    return (bookService.createBook(request.getFirstName(), request.getLastName(), request.getTitle()));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  public void updateBook(@PathVariable Long id, @Valid @RequestBody Request.RequestToUpdateBook request) {
    bookService.updateBook(id, request.title());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}")
  public void deleteBook(@PathVariable Long id) {
    String requestId = UUID.randomUUID().toString();
    if (authorRegistryGateway.checkBook(bookService.getBookInfo(id), requestId)) {
      bookService.deleteBook(id);
    }
  }

}
