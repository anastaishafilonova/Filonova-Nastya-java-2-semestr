package book.service.entity;

import book.service.controller.BookController;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.hibernate.sql.results.graph.Fetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "authors")
public class Author {
  private static final Logger logger = LoggerFactory.getLogger(Author.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  @NotNull(message = "firstname can not be null")
  private String firstName;

  @Column(name = "last_name")
  @NotNull(message = "Lastname can not be null")
  private String lastName;

  @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Book> bookList = new ArrayList<>();

  protected Author() {
  }

  public Author(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public List<Book> getBookList() {
    return bookList;
  }

  public void addBookInBookList(String title) {
    this.bookList.add(new Book(this, title));
    logger.info("Title: " + title);
    logger.info("Author: " + this);
  }

  public void deleteBookFromBookList(Long bookId) {
    bookList.removeIf(book -> Objects.equals(book.getId(), bookId));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Author)) return false;
    Author author = (Author) o;
    return id != null && id.equals(author.id);
  }

  @Override
  public int hashCode() {
    return Author.class.hashCode();
  }
}
