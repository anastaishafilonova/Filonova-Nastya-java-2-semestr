package book.service.service;

import book.service.entity.Book;
import book.service.entity.Tag;
import book.service.repository.BookRepository;
import book.service.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
  private TagRepository tagRepository;
  private BookRepository bookRepository;

  @Autowired
  public TagService(TagRepository tagRepository, BookRepository bookRepository) {
    this.tagRepository = tagRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public Tag createTag(String name, Long bookId) {
    Tag tag = new Tag(name);
    Book book = bookRepository.findById(bookId).orElseThrow();
    tag.addBook(book);
    return tagRepository.save(tag);
  }

  @Transactional
  public void updateTag(Long id, String name) {
    Tag tag = tagRepository.findById(id).orElseThrow();
    tag.setName(name);
    tagRepository.save(tag);
  }

  @Transactional
  public void deleteTag(Long id) {
    Tag tag = tagRepository.findById(id).orElseThrow();
    tagRepository.delete(tag);
  }
}
