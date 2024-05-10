package book.service.controller;

import book.service.service.BookPurchaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookPurchaseController {
  private final BookPurchaseService bookPurchaseService;

  @Autowired
  public BookPurchaseController(BookPurchaseService bookPurchaseService) {
    this.bookPurchaseService = bookPurchaseService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{userId}/pay/{bookId}")
  public void buyBook(@PathVariable Long userId, @PathVariable Long bookId) throws JsonProcessingException {
    bookPurchaseService.buyBook(bookId, userId);
  }
}
