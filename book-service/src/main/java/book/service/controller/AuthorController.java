package book.service.controller;

import book.service.controller.request.Request;
import book.service.controller.response.AuthorResponse;
import book.service.entity.Author;
import book.service.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/author")
public class AuthorController {
  private AuthorService authorService;

  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  protected AuthorController() {
  }


  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("")
  public Author createAuthor(@RequestBody Request.RequestToCreateOrUpdateAuthor request) {
    return authorService.createAuthor(request.getFirstName(), request.getLastName());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  public AuthorResponse updateAuthor(@PathVariable Long id, @RequestBody Request.RequestToCreateOrUpdateAuthor request) {
    return authorService.updateAuthor(id, request.getFirstName(), request.getLastName());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}")
  public void deleteAuthor(@PathVariable Long id) {
    authorService.deleteAuthor(id);
  }
}
