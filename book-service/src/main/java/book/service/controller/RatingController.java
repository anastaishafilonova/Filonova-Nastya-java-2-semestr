package book.service.controller;

import book.service.service.RatingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class RatingController {

  private final RatingService ratingService;

  @Autowired
  public RatingController(RatingService ratingService) {
    this.ratingService = ratingService;
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/books/{bookId}:getRating")
  public void countRating(@PathVariable @NotNull Long bookId) throws JsonProcessingException {
    ratingService.rating(bookId);
  }
}
