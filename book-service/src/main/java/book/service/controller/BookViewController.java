package book.service.controller;

import book.service.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import book.service.repository.BookRepository;

import java.util.List;

@Controller
public class BookViewController {
  private final BookRepository bookRepository;

  @Autowired
  public BookViewController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/books")
  public String viewBooks(Model model) {
    List<Book> books = bookRepository.findAllBy();
    model.addAttribute("books", books);
    return "books";
  }
}
