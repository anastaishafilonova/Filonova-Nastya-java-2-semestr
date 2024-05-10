package service.purchase.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "outbox")
public class Outbox {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String data;  // id книги, результат платежа

  public Outbox(String data) {
    this.data = data;
  }

  public Outbox() {
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}

