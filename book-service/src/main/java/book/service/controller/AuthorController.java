package book.service.controller;

import book.service.controller.request.Request;
import book.service.controller.response.AuthorResponse;
import book.service.entity.Author;
import book.service.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping("")
  public Author createAuthor(@RequestBody Request.RequestToCreateOrUpdateAuthor request) {
    return authorService.createAuthor(request.getFirstName(), request.getLastName());
  }

  @PutMapping("/{id}")
  public AuthorResponse updateAuthor(@PathVariable Long id, @RequestBody Request.RequestToCreateOrUpdateAuthor request) {
    return authorService.updateAuthor(id, request.getFirstName(), request.getLastName());
  }

  @DeleteMapping("/{id}")
  public void deleteAuthor(@PathVariable Long id) {
    authorService.deleteAuthor(id);
  }
}
