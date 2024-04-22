package book.service.repository;

import book.service.controller.response.BookResponse;
import book.service.controller.response.TagResponse;
import book.service.entity.Tag;
import book.service.service.AuthorService;
import book.service.service.BookService;
import book.service.service.DatabaseSuite;
import book.service.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({TagService.class, BookService.class, AuthorService.class})
class TagRepositoryTest extends DatabaseSuite {
  @Autowired
  TagRepository tagRepository;
  @Autowired
  TagService tagService;
  @Autowired
  BookService bookService;
  @Test
  public void findTagByNameTest() {
    BookResponse book = bookService.createBook("Андрей", "Курпатов", "Красная таблетка");
    Tag tag = tagService.createTag("Психология", book.id());
    Assertions.assertEquals(tagRepository.findTagByName("Психология").getId(), tag.getId());
  }
}
