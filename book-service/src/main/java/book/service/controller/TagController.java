package book.service.controller;

import book.service.controller.request.Request;
import book.service.controller.response.TagResponse;
import book.service.entity.Tag;
import book.service.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
public class TagController {
  private TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @PreAuthorize("hasAuthority('STUDENT')")
  @PostMapping("")
  public TagResponse createTag(@RequestBody Request.RequestToCreateTag request) {
    return new TagResponse(tagService.createTag(request.getText(), request.getBookId()).getName());
  }

  @PreAuthorize("hasAuthority('STUDENT')")
  @PutMapping("/{id}")
  public void updateTag(@PathVariable Long id, @RequestBody Request.RequestToUpdateTag request) {
    tagService.updateTag(id, request.name());
  }

  @PreAuthorize("hasAuthority('STUDENT')")
  @DeleteMapping("/{id}")
  public void deleteTag(@PathVariable Long id) {
    tagService.deleteTag(id);
  }
}
