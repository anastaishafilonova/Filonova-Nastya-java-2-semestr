package book.service.controller;

import book.service.entity.Author;
import book.service.entity.Book;
import book.service.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import book.service.repository.BookRepository;

import java.util.List;

@Controller
public class AuthorViewController {
  private final AuthorRepository authorRepository;

  @Autowired
  public AuthorViewController(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @GetMapping("/authors")
  public String viewAuthors(Model model) {
    List<Author> authors = authorRepository.findAllBy();
    model.addAttribute("authors", authors);
    return "authors";
  }
}