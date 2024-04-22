package book.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private Author author;
//  @NotNull(message = "Title can not be null")
  private String title;

  @ManyToMany(mappedBy = "books")
  private Set<Tag> tags = new HashSet<>();

  protected Book() {
  }

  public Book(Author author, String title) {
    this.author = author;
    this.title = title;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public long getId() {
    return id;
  }


  @Override
  public String toString() {
    return "author: " + this.author + "\n" + "title: " + this.title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Book)) return false;
    Book book = (Book) o;
    return id != null && id.equals(book.id);
  }

  @Override
  public int hashCode() {
    return Book.class.hashCode();
  }
}
