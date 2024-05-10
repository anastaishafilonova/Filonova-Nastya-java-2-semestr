package book.service.controller;

import book.service.entity.Book;
import book.service.entity.Tag;
import book.service.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import book.service.repository.BookRepository;

import java.util.List;

@Controller
public class TagViewController {
  private final TagRepository tagRepository;

  @Autowired
  public TagViewController(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @PreAuthorize("hasAuthority('STUDENT')")
  @GetMapping("/tags")
  public String viewTags(Model model) {
    List<Tag> tags = tagRepository.findAllBy();
    model.addAttribute("tags", tags);
    return "tags";
  }
}
