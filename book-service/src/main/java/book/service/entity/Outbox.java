package book.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "outbox")
public class Outbox {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String data;  // id книги, которую хотят купить, и id user-a

  public Outbox(String data) {
    this.data = data;
  }

  protected Outbox() {
  }

  public Long getId() {
    return id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Outbox outbox = (Outbox) o;
    return Objects.equals(id, outbox.id) && Objects.equals(data, outbox.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, data);
  }
}

