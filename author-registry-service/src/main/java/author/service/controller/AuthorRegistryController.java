package author.service.controller;

import author.service.service.AuthorRegistryService;
import author.service.service.BookInfo;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/author-registry")
public class AuthorRegistryController {
  private final AuthorRegistryService authorRegistryService;
  @Autowired
  public AuthorRegistryController(AuthorRegistryService authorRegistryService) {
    this.authorRegistryService = authorRegistryService;
  }

  @GetMapping("book")
  public AuthorRegistryResponse checkBook(@NotNull @RequestParam String firstName, String lastName, String title,
                             @NotNull @RequestHeader("X-REQUEST-ID") String requestId) {

    return new AuthorRegistryResponse(authorRegistryService.checkBook(requestId, new BookInfo(firstName, lastName, title)));
  }

  @GetMapping("create")
  public String createBook(@NotNull @RequestParam String firstName, String lastName, String title,
                         @NotNull @RequestHeader("X-REQUEST-ID") String requestId) {
    authorRegistryService.addBook(firstName, lastName, title, requestId);
    return "This book was added";
  }
}
