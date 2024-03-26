package book.service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "tags", uniqueConstraints= @UniqueConstraint(columnNames={"name"}))
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Name of tag can not be null")
  private String name;

  @ManyToMany(fetch = LAZY, cascade = PERSIST)
  @JoinTable(
      name = "tag_book",
      joinColumns = @JoinColumn(name = "tag_id"),
      inverseJoinColumns = @JoinColumn(name = "book_id")
  )
  private Set<Book> books = new HashSet<>();

  protected Tag() {}

  public Tag(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<Book> getBooks() {
    return books;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addBook(Book book) {
    books.add(book);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tag)) return false;
    Tag tag = (Tag) o;
    return id != null && id.equals(tag.id);
  }

  @Override
  public int hashCode() {
    return Tag.class.hashCode();
  }
}
