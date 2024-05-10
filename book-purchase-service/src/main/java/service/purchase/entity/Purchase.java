package service.purchase.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase")
public class Purchase {
  private int money;
  @Id
  private Long id;  // user_id

  public Purchase(int money, Long id) {
    this.money = money;
    this.id = id;
  }

  public Purchase() {
  }

  public int getMoney() {
    return money;
  }

  public void setMoney(int money) {
    this.money = money;
  }
}
